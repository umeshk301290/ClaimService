package com.prgx.workbench.claim.controller;

import java.util.LinkedHashMap;
import java.util.Objects;

import org.apache.commons.lang3.Validate;

import com.prgx.workbench.core.objectattachment.model.AttachmentContext;

/**
 * 
 * Util class for AttachmentContext handling
 *
 */
public class AttachmentContextUtil {

    private AttachmentContextUtil() {

    }

    /**
     * Creates a new AttachmentContext
     * 
     * @param projectId
     * @param dataViewId
     * @param dataSetId
     * @param type
     * @param commentId
     * @param entity
     * @param clientCode
     * @param attachmentContextType
     * @return
     */
    public static AttachmentContext getAttachmentContext(Long claimId,
            Long commentId, AttachmentType attachmentType, String clientCode, String attachmentContextType) {
        AttachmentContext attachmentContext = new AttachmentContext();
        attachmentContext.setType(attachmentContextType);
        LinkedHashMap<String, Object> context = new LinkedHashMap<>();
        Validate.notNull(claimId, "Claim id not found");
        Validate.notNull(clientCode, "Client Code not found");
        context.put("claimId", claimId);
        if (Objects.nonNull(commentId)) {
            context.put("comment", commentId);
        }
        if (Objects.nonNull(attachmentType)) {
            context.put("attachmentType", attachmentType);
        }
        if (Objects.nonNull(clientCode)) {
            context.put("clientCode", clientCode);
        }

        attachmentContext.setContext(context);
        return attachmentContext;
    }

}
