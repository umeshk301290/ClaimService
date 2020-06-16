package com.prgx.workbench.claim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgx.workbench.core.objectattachment.config.AttachmentProperties;
import com.prgx.workbench.core.objectattachment.handler.AttachmentRequestHandler;
import com.prgx.workbench.core.objectattachment.model.AttachmentContext;
import com.prgx.workbench.core.restclient.config.RestClientProvider;
import com.prgx.workbench.odata.dataview.model.DataView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.*;
import static com.prgx.workbench.claim.controller.AttachmentContextUtil.getAttachmentContext;

@Slf4j
@RestController
@RequestMapping("{clientCode}/claims({claimId})/documents")
public class AttachmentController {

	@Autowired
	private AttachmentRequestHandler attachmentRequestHandler;

	@Autowired
	RestClientProvider restClientProvider;

	/**
	 * The attachment properties.
	 */
	@Autowired
	private AttachmentProperties attachmentProperties;

	@Autowired
	private AttachmentReaderService readerService;

	@Value("${endpoint.objectstore.document}")
	private String objectStoreEndPoint;

	@Value("${endpoint.objectstore.detail}")
	private String objectStoreDetailEndpoint;

	@Value("${prgx.attachment.document-attachment.service-url}")
	private String serviceUri;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public void getAttachments(HttpServletRequest request, HttpServletResponse httpServletResponse,
			@PathVariable("claimId")
					Long claimId,
			@RequestParam("attachType")
					AttachmentType attachmentType,
			@PathVariable("clientCode")
					String clientCode)  {
		
		 AttachmentContext attachmentContext = getAttachmentContext(claimId, null,attachmentType,
	                clientCode, attachmentProperties.getDocumentAttachment().getContext());
		attachmentRequestHandler.handleRequest(objectStoreEndPoint, attachmentContext, request, httpServletResponse);
	}

	@GetMapping(value = "{documentId}/content", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getContent(
			@PathVariable("claimId")
					Long claimId,
			@PathVariable("documentId")
					String documentId,
			@PathVariable("clientCode")
					String clientCode,
			@RequestParam(value = "$top", required = false)
					Integer recordCount) {
		try {
			ExceptionDetailMetadata metadata = getDocumentMetataData( documentId, clientCode);
			Collection<DataView> dataViews = readerService
					.fetchClaimSupportingData(clientCode, AttachmentReaderService.DocumentType.CSV, documentId, metadata, recordCount);
			List<Map<String, Object>> csvData = new LinkedList<>();
			dataViews.stream().forEach(dataview -> csvData.add(dataview.getValues()));
			return new ResponseEntity(csvData, HttpStatus.OK);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity("Document Not found. Eror Generated : " + e.getMessage(), HttpStatus.NOT_FOUND);
		}

	}

	public ExceptionDetailMetadata getDocumentMetataData( String documentId, String clientCode) throws IOException {
		RestTemplate restTemplate = restClientProvider.getRestTemplateWithToken();
		ObjectMapper mapper = new ObjectMapper();
		String endpoint = MessageFormat.format(objectStoreDetailEndpoint, clientCode, documentId);
		StringBuilder apiUrl = new StringBuilder().append(serviceUri).append('/').append(endpoint);
		ResponseEntity<DocumentObjectBean> bean = restTemplate
				.exchange(URI.create(apiUrl.toString()), HttpMethod.GET, getRequestEntity(apiUrl.toString()),
						DocumentObjectBean.class);
		DocumentObjectBean documentObjectBean = bean.getBody();
		ExceptionDetailMetadata metadata = new ExceptionDetailMetadata();
		metadata = mapper.convertValue(documentObjectBean.getFileMetadata(), ExceptionDetailMetadata.class);
		return metadata;
	}

	private RequestEntity getRequestEntity(String apiUrl) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		return new RequestEntity(null, header, HttpMethod.GET, URI.create(apiUrl));
	}
}
