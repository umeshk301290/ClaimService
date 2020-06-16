package com.prgx.workbench.claim.document.metadata;

import org.springframework.stereotype.Component;

import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.odata.jpa.mapper.impl.BaseMapper;

@Component
public class DocumentMetaDataConverter extends BaseMapper<DocumentMetaData> {
	
    @Override
    public BaseMapper<?> getMapper(String fieldName) {
        return null;
    }

    @Override
    public Class<DocumentMetaData> getObjectClass() {
        return DocumentMetaData.class;
    }

    @Override
    public PropertyProvider[] getPropertyProviders() {
        return DocumentMetaDataProperty.values();
    }

}
