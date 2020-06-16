package com.prgx.workbench.claim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgx.workbench.claim.exception.ClaimServiceExceptionMessages;
import com.prgx.workbench.core.objectattachment.config.AttachmentProperties;
import com.prgx.workbench.core.objectattachment.config.ServiceProperties;
import com.prgx.workbench.core.objectattachment.handler.AttachmentRequestHandler;
import com.prgx.workbench.core.objectattachment.model.AttachmentContext;
import com.prgx.workbench.exception.ApiRuntimeException;
import com.prgx.workbench.exception.message.GenericMessageCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static com.prgx.workbench.claim.controller.TemplateAttachmentContextUtil.getAttachmentContext;

@RestController
@RequestMapping("{clientCode}/templateAttachment")
public class TemplateAttachmentController {

	@Autowired
	private AttachmentRequestHandler attachmentRequestHandler;

	@Autowired
	private AttachmentProperties attachmentProperties;

	@Value("${claim.supported.extension}")
	private String supportedExtension;

	@RequestMapping(value = "doc-attach/**", method = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
			RequestMethod.PATCH, RequestMethod.DELETE }, consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.MULTIPART_FORM_DATA_VALUE })
	public void attachments(HttpServletRequest request, HttpServletResponse httpServletResponse,
			@PathVariable("clientCode") String clientCode) throws IOException {
		validateFilesandMetadata(request);

		handleRequest(request, httpServletResponse, AttachmentType.CLAIM_PACKAGE_COMPONENT_TEMPLATES, clientCode,
				"/doc-attach/", attachmentProperties.getDocumentAttachment());
	}

	protected void handleRequest(HttpServletRequest request, HttpServletResponse httpServletResponse,
			AttachmentType attachmentType, String clientCode, String apiUrl, ServiceProperties serviceProperties) {
		AttachmentContext attachmentContext = getAttachmentContext(clientCode, attachmentType,
				serviceProperties.getContext());
		String baseEntity = StringUtils.EMPTY;
		String baseUrl = baseEntity + StringUtils.substringAfter(request.getRequestURI(), apiUrl);
		attachmentRequestHandler.handleRequest(baseUrl, attachmentContext, request, httpServletResponse);
	}

	private void validateMetadata(HttpServletRequest request) {
		MetadataRequestString metadataRequest = null;
		ObjectMapper mapper = new ObjectMapper();
		String requestString = request.getParameter("request");
		if (Objects.isNull(requestString)) {
			throw new ApiRuntimeException(GenericMessageCode.MSG_ERR_SYS_MISSING_PARAMETER, new Object[] { "request" },
					HttpStatus.BAD_REQUEST.value());
		}
		try {
			metadataRequest = mapper.readValue(requestString, MetadataRequestString.class);
			for (String ket : metadataRequest.fileMetadata.keySet()) {
				ExceptionAttachmentMetadata metadata = mapper.readValue(metadataRequest.fileMetadata.get(ket),
						ExceptionAttachmentMetadata.class);
				validateMetadataFields(metadata);
			}
		} catch (IOException e) {
			throw new ApiRuntimeException(GenericMessageCode.MSG_ERR_SYS_INVALID_ARGUMENT,
					new Object[] { requestString, "Invalid Metadata Structure." }, e);
		}
	}

	private void validateMetadataFields(ExceptionAttachmentMetadata metadata) {
		if (Objects.nonNull(metadata)) {
			Optional.ofNullable(metadata.getAliasName()).ifPresent(aliasName -> {
				if (aliasName.length() > 50)
					throw new ApiRuntimeException(ClaimServiceExceptionMessages.INVALID_LENGTH,
							new Object[] { "AliasName", 50 });
			});
			Optional.ofNullable(metadata.getObjectId())
					.orElseThrow(() -> new ApiRuntimeException(ClaimServiceExceptionMessages.REQUIRED_FIELD,
							new Object[] { "objectId", metadata.getFileName() }));
			Optional.ofNullable(metadata.getObjectValueId())
					.orElseThrow(() -> new ApiRuntimeException(ClaimServiceExceptionMessages.REQUIRED_FIELD,
							new Object[] { "objectValueId", metadata.getFileName() }));
		}
	}

	private void validateFilesandMetadata(HttpServletRequest request) throws IOException {

		if (request instanceof MultipartRequest && RequestMethod.POST.name().equals(request.getMethod())) {
			validateMetadata(request);
			MultipartRequest partRequest = (MultipartRequest) request;
			Iterator fileIterator = partRequest.getFileNames();
			while (fileIterator.hasNext()) {
				String fileNameParameter = (String) fileIterator.next();
				List<MultipartFile> files = partRequest.getFiles(fileNameParameter);
				Set<String> fileNameSet = new LinkedHashSet<>();
				files.forEach((file) -> fileNameSet.add(file.getOriginalFilename()));
				if (files.size() != fileNameSet.size()) {
					throw new ApiRuntimeException(ClaimServiceExceptionMessages.DUPLICATE_FILES_IN_REQUEST, null,
							HttpStatus.BAD_REQUEST.value());
				}
				fileNameSet.forEach(fileName -> {
					if (!fileName.endsWith(supportedExtension)) {
						throw new ApiRuntimeException(ClaimServiceExceptionMessages.UNSUPPORTED_EXTENSION,
								new Object[] { "fileExtension" }, HttpStatus.BAD_REQUEST.value());
					}
				});
			}
		}
	}
}
