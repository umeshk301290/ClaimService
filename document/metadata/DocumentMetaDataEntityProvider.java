package com.prgx.workbench.claim.document.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.core.odata.entity.provider.impl.AbstractEntityProvider;
import com.prgx.workbench.core.odata.spring.OdataProperties;

@Component
public class DocumentMetaDataEntityProvider extends AbstractEntityProvider {

	public static final String ET_NAME = "objectstoremetadatas";
	public static final String ES_NAME = "objectstoremetadatas";

	@Autowired
	private OdataProperties odataProperties;

	@Override
	public PropertyProvider[] getPropertyProviders() {
		return DocumentMetaDataProperty.values();
	}

	@Override
	public String getEntityName() {
		return ET_NAME;
	}

	@Override
	public String getEntitySetName() {
		return ES_NAME;
	}

	@Override
	public OdataProperties getOdataProperties() {
		return odataProperties;
	}

}
