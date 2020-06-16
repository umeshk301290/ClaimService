
package com.prgx.workbench.claim.document.metadata.service;

import com.prgx.workbench.claim.document.metadata.DocumentMetaData;
import com.prgx.workbench.claim.document.metadata.DocumentMetaDataService;
import com.prgx.workbench.core.objectattachment.metadata.service.DocumentMetadataService;
import com.prgx.workbench.core.objectattachment.model.AttachmentContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service

@Slf4j
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

	private final DocumentMetaDataService documentMetaDataService;

	private final DocumentMetadataHelper documentMetadataHelper;

	@Autowired
	public DocumentMetadataServiceImpl(DocumentMetaDataService documentMetaDataService,
			DocumentMetadataHelper documentMetadataHelper) {
		this.documentMetaDataService = documentMetaDataService;
		this.documentMetadataHelper = documentMetadataHelper;
	}

	@Override
	public Object preSave(MultiValueMap<String, Object> requestData, AttachmentContext context) {
		return documentMetadataHelper.preSave(requestData, context);
	}

	@Override

	@Transactional
	public void save(Object documentData, List<Map<String, Object>> responseBody, AttachmentContext attachmentContext) {
		List<DocumentMetaData> documentMetaDataList = documentMetadataHelper.transformForSave(documentData, responseBody, attachmentContext);
		Optional.ofNullable(documentMetaDataList).ifPresent(documentMetaDataList1 -> { documentMetaDataList1.forEach(documentMetaData ->
			documentMetaDataService.save(documentMetaData));
		});
	}

	@Override
	public Object preUpdate(String documentId, Object requestData, AttachmentContext context) {
		return documentMetadataHelper.preUpdate(documentId, requestData, context);
	}

	@SuppressWarnings("unchecked")

	@Override

	@Transactional
	public void update(String documentId, Object dataToUpdate, AttachmentContext attachmentContext) {
		if (Objects.isNull(dataToUpdate)) {
			log.info("Nothing to update");
			return;
		}
		if (dataToUpdate instanceof List) {
			((List<DocumentMetaData>) dataToUpdate).forEach(documentMetaData -> {
				documentMetaData.setCode(attachmentContext.getCode());
				documentMetaDataService.update(documentMetaData);
			});
		} else {
			DocumentMetaData documentMetaData = (DocumentMetaData) dataToUpdate;
			documentMetaData.setDocumentMetaDataId(UUID.fromString(documentId));
			documentMetaData.setCode(attachmentContext.getCode());
			documentMetaDataService.update(documentMetaData);
		}
	}

	@Override
	@Transactional
	public void delete(String documentId, AttachmentContext context) {
		documentMetaDataService.delete(UUID.fromString(documentId));
	}

	@Override
	@Transactional
	public void getDocumentMetadataByCode(HttpServletRequest request, HttpServletResponse response, AttachmentContext context) {
		documentMetaDataService.getObjectStoreMetaDataByCode(request, response, context.getCode());
	}

}
