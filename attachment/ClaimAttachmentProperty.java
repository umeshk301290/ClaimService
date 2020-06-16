package com.prgx.workbench.claim.attachment;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.prgx.workbench.core.odata.entity.provider.EntityProperty;
import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;

public enum ClaimAttachmentProperty implements PropertyProvider {

	TYPE(String.class, "type", true), COUNT(Long.class, "count"), UNREAD(Long.class, "unread");

	private final EntityProperty entityProperty;

	private ClaimAttachmentProperty(Class<?> javaClass, String odataPropertyName) {
		this(javaClass, odataPropertyName, false);
	}

	private ClaimAttachmentProperty(Class<?> javaClass, String odataPropertyName, boolean id) {
		this(javaClass, odataPropertyName, odataPropertyName, null, id);
	}

	private ClaimAttachmentProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName,
			FullQualifiedName fullQualifiedName, boolean id) {
		this.entityProperty = EntityProperty.builder().javaClass(javaClass).odataPropertyName(odataPropertyName)
				.entityPropertyName(entityPropertyName).fullQualifiedName(fullQualifiedName).id(id).build();
	}

	public EntityProperty getEntityProperty() {
		return entityProperty;
	}

}
