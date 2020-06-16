package com.prgx.workbench.claim.document.metadata.service;

import java.io.IOException;
import java.util.*;

import com.prgx.workbench.claim.Claim;
import com.prgx.workbench.claim.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.prgx.workbench.claim.controller.MetadataRequestString;
import com.prgx.workbench.claim.document.metadata.DocumentMetaData;
import com.prgx.workbench.core.objectattachment.model.AttachmentContext;
import com.prgx.workbench.core.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@SuppressWarnings("unchecked")
public class DocumentMetadataHelper {

	private final DocumentAttachmentProperties documentAttachmentProperties;
	private ClaimService claimService;

	private static final String PROJECT_VENDOR_ID = "projectVendorId";
	private static final String PROJECT_ID = "projectId";
	private static final String FILE_NAME = "fileName";
	private static final String MODULE_ID = "moduleId";
	private static final String DOC_TYPE = "docType";
	private static final String ALIAS_NAME = "aliasName";
	private static final String FILE_ORDER = "fileOrder";
	private static final String DOC_STATUS = "docStatus";
	private static final String COMMENT = "comment";
	private static final String CLAIM_PACKAGE = "claimPackage";
	private static final String OBJECT_ID = "objectId";
	private static final String OBJECT_VALUE_ID = "objectValueId";
	private static final String VISIBILITY_LEVEL = "visibilityLevel";
	private static final String DOCUMENT_ID = "documentId";
	private static final String REQUEST_PARAMETER = "request";
	private static final String FILE_METADATA = "fileMetadata";
	private static final String ATTACHMENT_TYPE = "attachmentType";
	private static final String APP_VENDOR_ID = "appVendorId";
	private static final String SEND_FROM = "sendFrom";


	@Autowired
	public DocumentMetadataHelper(DocumentAttachmentProperties documentAttachmentProperties, ClaimService claimService) {
		this.documentAttachmentProperties = documentAttachmentProperties;
		this.claimService = claimService;
	}

	/** The Constant POST Request Parameter TYPE. */
	protected static final TypeReference<Map<String, Map<String, Map<String, Object>>>> POST_REQUEST_TYPE = new TypeReference<Map<String, Map<String, Map<String, Object>>>>() {
	};

	public DocumentMetaData convertToMetaDataObject(String documentId, Map<String, Object> metaDataObject) {
		DocumentMetaData documentMetaData = new DocumentMetaData();
		
		documentMetaData.setDocumentMetaDataId(Objects.isNull(documentId) ? null : UUID.fromString(documentId));
		documentMetaData.setProjectId(
				isNull(metaDataObject.get(PROJECT_ID)) ? null : (Long.valueOf(getValue(metaDataObject, PROJECT_ID))));
		documentMetaData.setAppVendorId(
				isNull(metaDataObject.get(APP_VENDOR_ID)) ? null : UUID.fromString(getValue(metaDataObject, APP_VENDOR_ID)));
		documentMetaData.setProjectVendorId(
				isNull(metaDataObject.get(PROJECT_VENDOR_ID)) ? null : (Long.valueOf(getValue(metaDataObject, PROJECT_VENDOR_ID))));
		documentMetaData
				.setDocumentName(isNull(metaDataObject.get(FILE_NAME)) ? null : getValue(metaDataObject, FILE_NAME));
		documentMetaData.setModuleId(
				isNull(metaDataObject.get(MODULE_ID)) ? null : Long.valueOf(getValue(metaDataObject, MODULE_ID)));
		documentMetaData.setDocumentTypeId(
				isNull(metaDataObject.get(DOC_TYPE)) ? null : Long.valueOf(getValue(metaDataObject, DOC_TYPE)));
		documentMetaData
				.setAliasFileName(isNull(metaDataObject.get(ALIAS_NAME)) ? null : getValue(metaDataObject, ALIAS_NAME));
		documentMetaData.setFileOrder(
				isNull(metaDataObject.get(FILE_ORDER)) ? null : Long.valueOf(getValue(metaDataObject, FILE_ORDER)));
		documentMetaData.setDocumentStatus(
				isNull(metaDataObject.get(DOC_STATUS)) ? null : getValue(metaDataObject, DOC_STATUS));
		documentMetaData.setComment(isNull(metaDataObject.get(COMMENT)) ? null : getValue(metaDataObject, COMMENT));
		documentMetaData.setClaimPackage(isNull(metaDataObject.get(CLAIM_PACKAGE)) ? Boolean.FALSE
				: Boolean.valueOf(getValue(metaDataObject, CLAIM_PACKAGE)));
		documentMetaData
				.setObjectId(isNull(metaDataObject.get(OBJECT_ID)) ? null : getValue(metaDataObject, OBJECT_ID));
		documentMetaData.setObjectValueId(
				isNull(metaDataObject.get(OBJECT_VALUE_ID)) ? null : getValue(metaDataObject, OBJECT_VALUE_ID));
		documentMetaData.setVisibilityLevel(isNull(metaDataObject.get(VISIBILITY_LEVEL)) ? 0
				: Integer.parseInt(getValue(metaDataObject, VISIBILITY_LEVEL)));
		documentMetaData.setSendFrom(
				isNull(metaDataObject.get(SEND_FROM)) ? null : getValue(metaDataObject, SEND_FROM));
		return documentMetaData;
	}

	private UUID getAppVendorId(Long claimId) {
		Claim claim = claimService.getEntity(claimId);
		if(Objects.isNull(claim))
			return null;
		return claim.getAppVendorId();
	}


	public Object preSave(MultiValueMap<String, Object> requestData, AttachmentContext attachmentContext) {
		if(!processDcoumentRecord(attachmentContext)){
			return null;
		}
		List<Object> requestParamData = requestData.get(REQUEST_PARAMETER);
		if (Objects.nonNull(requestParamData) && requestParamData.size() > 0) {
			try {
				MetadataRequestString metadataRequest = JsonUtils.fromJson(String.valueOf(requestParamData.get(0)),
						MetadataRequestString.class);
				Map<String, DocumentMetaData> fileDocumentMetaDataMap = new HashMap<>();
				metadataRequest.getFileMetadata().keySet().forEach(fileName -> {
					try {
						Map<String, Object> fileMetaData = (Map<String, Object>) JsonUtils
								.fromJson(metadataRequest.getFileMetadata().get(fileName), Object.class);
						fileDocumentMetaDataMap.put(fileName, processRequestMetaData(null, fileMetaData,attachmentContext));
						metadataRequest.getFileMetadata().put(fileName, JsonUtils.toJson(fileMetaData));
					} catch (IOException exception) {
						log.error("Error during parsing request {}", exception.getMessage());
					}
				});
				requestParamData.set(0, JsonUtils.toJson(metadataRequest));
				return fileDocumentMetaDataMap;
			} catch (IOException exception) {
				log.error("Error during parsing request {}", exception.getMessage());
			}
		} else {
			log.info("File metadata not found in request");
		}
		return null;
	}

	public List<DocumentMetaData> transformForSave(Object documentData, List<Map<String, Object>> responseBody,
			AttachmentContext attachmentContext) {
		if(!processDcoumentRecord(attachmentContext)){
			return null;
		}
		Map<String, DocumentMetaData> documentMap = (Map<String, DocumentMetaData>) documentData;
		List<DocumentMetaData> documentMetaDatas = new ArrayList<>();
		responseBody.forEach(fileObject -> {
			DocumentMetaData documentMetaData = documentMap
					.get(((Map<String, Object>) fileObject.get(FILE_METADATA)).get(DocumentMetadataHelper.FILE_NAME));
			documentMetaData.setId(UUID.fromString(((String) fileObject.get(DocumentMetadataHelper.DOCUMENT_ID))));
			documentMetaData.setCode(attachmentContext.getCode());
			documentMetaDatas.add(documentMetaData);
		});
		return documentMetaDatas;
	}

	public Object preUpdate(String documentId, Object requestData, AttachmentContext context) {
		if (requestData instanceof List) {
			List<DocumentMetaData> documentMetaDatas = new ArrayList<>();
			((List<Map<String, Object>>) requestData).forEach(data -> documentMetaDatas.add(processPutRequestData(data, context)));
			return documentMetaDatas;
		} else {
			((Map<String, Object>) requestData).put(DocumentMetadataHelper.DOCUMENT_ID, documentId);
			return processPutRequestData((Map<String, Object>) requestData, context);
		}
	}

	private DocumentMetaData processPutRequestData(Map<String, Object> requestData, AttachmentContext attachmentContext) {
		Map<String, Object> requestMetaData = (Map<String, Object>) requestData.get(FILE_METADATA);
		return processRequestMetaData((String) requestData.get(DocumentMetadataHelper.DOCUMENT_ID), requestMetaData, attachmentContext);
	}

	private DocumentMetaData processRequestMetaData(String documentId, Map<String, Object> requestMetaData,
			AttachmentContext attachmentContext) {
		if(Objects.nonNull(attachmentContext.getContext()) && Objects.nonNull(attachmentContext.getContext().get("claimId"))) {
			Long claimId = Long.valueOf(String.valueOf(attachmentContext.getContext().get("claimId")));
			if(isNull(requestMetaData.get(APP_VENDOR_ID)))
				requestMetaData.put(APP_VENDOR_ID,getAppVendorId(claimId));
		}
		DocumentMetaData documentMetaData = convertToMetaDataObject(documentId, requestMetaData);
		requestMetaData.keySet().removeAll(documentAttachmentProperties.getRemoveMetaDataPropertiesObjectStore());
		return documentMetaData;
	}

	private boolean isNull(Object value) {
		return Objects.isNull(value);
	}

	private String getValue(Map<String, Object> metaDataObject, String fieldName) {
		return String.valueOf(metaDataObject.get(fieldName));
	}

	private boolean processDcoumentRecord(AttachmentContext attachmentContext) {
		boolean processRecord = true;
		Map<String, Object> context = attachmentContext.getContext();
		if (Objects.nonNull(context) && Objects.nonNull(context.get(ATTACHMENT_TYPE))) {
			processRecord = false;
		}
		return processRecord;
	}


}
