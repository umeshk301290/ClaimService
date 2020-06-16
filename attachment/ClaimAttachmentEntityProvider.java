package com.prgx.workbench.claim.attachment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.core.odata.entity.provider.impl.AbstractEntityProvider;
import com.prgx.workbench.core.odata.spring.OdataProperties;


@Component
public class ClaimAttachmentEntityProvider extends AbstractEntityProvider {

	/** The entity name. */
	public static final String ET_NAME = "attachment";

	/** The entity set name. */
	public static final String ES_NAME = "attachments";

	@Autowired
	private OdataProperties odataProperties;

	@Override
	public PropertyProvider[] getPropertyProviders() {
		return ClaimAttachmentProperty.values();
	}

	@Override
	protected String getEntityName() {
		return ET_NAME;
	}

	@Override
	protected String getEntitySetName() {
		return ES_NAME;
	}

	@Override
	protected OdataProperties getOdataProperties() {
		return odataProperties;
	}
}
