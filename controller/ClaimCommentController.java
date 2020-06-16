package com.prgx.workbench.claim.controller;

import static com.prgx.workbench.claim.controller.AttachmentContextUtil.getAttachmentContext;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgx.workbench.claim.exception.ClaimServiceExceptionMessages;
import com.prgx.workbench.core.objectattachment.config.AttachmentProperties;
import com.prgx.workbench.core.objectattachment.config.ServiceProperties;
import com.prgx.workbench.core.objectattachment.handler.AttachmentRequestHandler;
import com.prgx.workbench.core.objectattachment.model.AttachmentContext;
import com.prgx.workbench.exception.ApiRuntimeException;
import com.prgx.workbench.exception.message.ErrorMessageDetail;
import com.prgx.workbench.exception.message.GenericMessageCode;
import com.prgx.workbench.exception.message.MessageDetail;
import com.prgx.workbench.exception.message.Messages;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("{clientCode}/claims({claimId})")
@Slf4j
public class ClaimCommentController {


	/** The attachment properties. */
	@Autowired
	private AttachmentProperties attachmentProperties;

	/** The attachment request handler. */
	@Autowired
	private AttachmentRequestHandler attachmentRequestHandler;

	@Value("${audit.attachments.unsupportedExtensions:.exe,.mdb}")
	private String unsupportedExtensions;

	private static final String COMMA = ",";

	@Autowired
	Messages messages;
	/**
	 * Comments.
	 *
	 * @param request
	 *            the request
	 * @param httpServletResponse
	 *            the http servlet response
	 * @param claimId
	 *            the claim id
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 */
	@RequestMapping(value = "comment-attach/**", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
			RequestMethod.PATCH, RequestMethod.DELETE }, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public void comments(HttpServletRequest request, HttpServletResponse httpServletResponse,
			@PathVariable("claimId") Long claimId,@PathVariable("clientCode")
			String clientCode) throws URISyntaxException {
		handleRequest(request, httpServletResponse, claimId, null, "/comment-attach/",clientCode,
				attachmentProperties.getComment());
	}

	protected void handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse, Long claimId,
			Long commentId, String apiUrl, String clientCode,ServiceProperties serviceProperties) {
		
		 AttachmentContext attachmentContext = getAttachmentContext(claimId, commentId,null,
	                clientCode, serviceProperties.getContext());
		String baseEntity = StringUtils.EMPTY;
		String baseUrl = baseEntity + StringUtils.substringAfter(request.getRequestURI(), apiUrl);
		attachmentRequestHandler.handleRequest(baseUrl, attachmentContext, request, httpServletResponse);
	}

	/**
	 * Comment attachments.
	 *
	 * @param request
	 *            the request
	 * @param httpServletResponse
	 *            the http servlet response
	 * @param claimId
	 *            the claim id
	 * @param commentId
	 *            the comment id
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 */
	@RequestMapping(value = "comments({commentId})/doc-attach/**", method = { RequestMethod.GET, RequestMethod.POST,
			RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
	public void commentAttachments(HttpServletRequest request, HttpServletResponse httpServletResponse,
			@PathVariable("claimId") Long claimId, @PathVariable("commentId") Long commentId,@PathVariable("clientCode")
			String clientCode)
			throws URISyntaxException {
		Validate.notNull(commentId, "Comment id not found");
		handleRequest(request, httpServletResponse, claimId, commentId, "/doc-attach/",clientCode,
				attachmentProperties.getCommentsAttachment());
	}

	@RequestMapping(value = "doc-attach/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
			RequestMethod.PATCH, RequestMethod.DELETE}, consumes = {MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE})
	public void attachments(HttpServletRequest request, HttpServletResponse httpServletResponse,
			@PathVariable("claimId")
					Long claimId,
			@PathVariable("clientCode")
					String clientCode
	) throws URISyntaxException, IOException {
		validateFilesandMetadata(request);
		handleRequest(request, httpServletResponse,claimId,null,"/doc-attach/", clientCode, attachmentProperties.getDocumentAttachment());
	}

	private void validateMetadata(HttpServletRequest request) {
		String requestString = request.getParameter("request");
		if (Objects.isNull(requestString))
			throw new ApiRuntimeException(GenericMessageCode.MSG_ERR_SYS_MISSING_PARAMETER, new Object[] {"request"},
					HttpStatus.BAD_REQUEST.value());
		MetadataRequestString metadataRequest = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			metadataRequest = mapper.readValue(requestString, MetadataRequestString.class);
			for (String ket : metadataRequest.fileMetadata.keySet()) {
				ExceptionAttachmentMetadata metadata = mapper
						.readValue(metadataRequest.fileMetadata.get(ket), ExceptionAttachmentMetadata.class);
				validateMetadataFields(metadata);
			}
		} catch (IOException e) {
			throw new ApiRuntimeException(GenericMessageCode.MSG_ERR_SYS_INVALID_ARGUMENT,
					new Object[] {requestString, "Invalid Metadata Structure."},e);
		}
	}

	private void validateMetadataFields(ExceptionAttachmentMetadata metadata) {
		if(Objects.nonNull(metadata)) {
			Optional.ofNullable(metadata.getDocType()).orElseThrow(
					() -> new ApiRuntimeException(ClaimServiceExceptionMessages.REQUIRED_FIELD,
							new Object[]{"docType",metadata.getFileName()}));

			Optional.ofNullable(metadata.getAliasName()).ifPresent((aliasName) -> {
				if (aliasName.length() > 50)
					throw new ApiRuntimeException(ClaimServiceExceptionMessages.INVALID_LENGTH,
							new Object[]{"AliasName", 50});
			});
		}
	}

	private void validateFilesandMetadata(HttpServletRequest request) throws IOException {

		if (request instanceof MultipartRequest && RequestMethod.POST.name().equals(request.getMethod())) {
			validateMetadata(request);
			List<String> invalidExtennsions = Arrays.asList(unsupportedExtensions.split(COMMA));
			MultipartRequest partRequest = (MultipartRequest) request;
			Iterator fileIterator = partRequest.getFileNames();
			while (fileIterator.hasNext()) {
				String fileNameParameter = (String) fileIterator.next();
				List<MultipartFile> files = partRequest.getFiles(fileNameParameter);
				Set<String> fileNameSet = new LinkedHashSet();

				files.stream().forEach((file) -> {
					fileNameSet.add(file.getOriginalFilename());
				});

				if (files.size() != fileNameSet.size()) {
					throw new ApiRuntimeException(ClaimServiceExceptionMessages.DUPLICATE_FILES_IN_REQUEST, null,
							HttpStatus.BAD_REQUEST.value());
				}
				StringJoiner invalidExtensionJoiner = new StringJoiner(COMMA);
				fileNameSet.forEach(fileName -> {
					invalidExtennsions.forEach((extenstion) -> {
						if (fileName.endsWith(extenstion)) {
							invalidExtensionJoiner.add(fileName);
						}
					});
				});
				String InvalidExtensionFiles = invalidExtensionJoiner.toString();
				if (!InvalidExtensionFiles.isEmpty()) {
					throw new ApiRuntimeException(ClaimServiceExceptionMessages.UNSUPPORTED_EXTENSION,
							new Object[] {InvalidExtensionFiles}, HttpStatus.BAD_REQUEST.value());
				}
			}
		}
	}

	@ExceptionHandler(ApiRuntimeException.class)
	public ResponseEntity<ErrorMessageDetail> controllerExceptionHandler(ApiRuntimeException apiRuntimeException) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		MessageDetail message = messages.get(apiRuntimeException.getCode(), apiRuntimeException.getArguments());
		ResponseEntity<ErrorMessageDetail> errorMessageDetailResponseEntity = new ResponseEntity<>(new ErrorMessageDetail(message),
				headers, HttpStatus.valueOf(apiRuntimeException.getStatusCode().orElseGet(()->HttpStatus.BAD_REQUEST.value())));
		return errorMessageDetailResponseEntity;
	}
}
