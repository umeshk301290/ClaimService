package com.prgx.workbench.claim.attachment;

import org.springframework.stereotype.Component;

import com.prgx.workbench.core.objectattachment.model.AttachmentStatusResponse;
import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.odata.jpa.mapper.impl.BaseMapper;

@Component
public class ClaimAttachmentStatusMapper extends  BaseMapper<AttachmentStatusResponse> {
    
    @Override
    protected Class<AttachmentStatusResponse> getObjectClass() {
        return AttachmentStatusResponse.class;
    }

    @Override
    protected PropertyProvider[] getPropertyProviders() {
        return ClaimAttachmentProperty.values();
    }

    @Override
    protected BaseMapper<?> getMapper(String fieldName) {
        return null;
    }

}
