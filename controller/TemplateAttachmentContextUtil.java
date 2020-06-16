package com.prgx.workbench.claim.controller;

import com.prgx.workbench.core.objectattachment.model.AttachmentContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * Util class for AttachmentContext handling
 *
 */
public class TemplateAttachmentContextUtil {

    private TemplateAttachmentContextUtil() {
    }

    public static AttachmentContext getAttachmentContext(String clientCode, AttachmentType attachmentType,
                                                         String attachmentContextType) {
        AttachmentContext attachmentContext = new AttachmentContext();
        attachmentContext.setType(attachmentContextType);
        Map<String, Object> context = new LinkedHashMap<>();
        if (Objects.nonNull(attachmentType)) {
            context.put("templateAttachmentType", attachmentType);
        }
        context.put("clientCode", clientCode);
        attachmentContext.setContext(context);
        return attachmentContext;
    }
}