package com.prgx.workbench.claim.template;

import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.odata.jpa.mapper.impl.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public class ClaimTemplateMappingConverter extends BaseMapper<ClaimTemplateMapping> {



	@Override protected BaseMapper<?> getMapper(String fieldName) {

			return null;

	}

	@Override protected Class<ClaimTemplateMapping> getObjectClass() {
		return ClaimTemplateMapping.class;
	}

	@Override protected PropertyProvider[] getPropertyProviders() {
		return ClaimTemplateMappingProperty.values();
	}

}

