package com.prgx.workbench.claim.document.metadata;

import java.util.Date;
import java.util.UUID;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.prgx.workbench.core.odata.entity.provider.EntityProperty;
import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;

public enum DocumentMetaDataProperty implements PropertyProvider {

    DOCUMENT_META_DATA_ID(UUID.class,"documentMetaDataId",true),
    PROJECT_ID(Long.class, "projectId"),
    PROJECT_VENDOR_ID(Long.class, "projectVendorId"),
    FILE_ORDER(Long.class, "fileOrder"),
    MODULE_ID(Long.class, "moduleId"),
    DOCUMENT_TYPE_ID(Long.class, "documentTypeId"),
    DOCUMENT_NAME(String.class, "documentName"),
    ALIAS_FILENAME(String.class, "aliasFileName"),
    DOCUMENT_STATUS(String.class, "documentStatus"),
    CLAIM_PACKAGE(Boolean.class, "claimPackage"),
    OBJECT_ID(String.class, "objectId"),
    OBJECT_VALUE_ID(String.class, "objectValueId"),
    COMMENT(String.class, "comment"),
    VISIBILITY_lEVEL(Integer.class, "visibilityLevel"),
    CODE(String.class, "code"),
    CREATED_DATE(Date.class, "createdDate"),
    CREATED_BY(String.class, "createdBy"),
    LAST_MODIFIED_DATE(Date.class, "lastModifiedDate"),
    LAST_MODIFIED_BY(String.class, "lastModifiedBy"),
    APP_VENDOR_ID(UUID.class, "appVendorId"),
    SEND_FROM(String.class, "sendFrom");

    private final EntityProperty entityProperty;

    private DocumentMetaDataProperty(Class<?> javaClass, String odataPropertyName) {
        this(javaClass, odataPropertyName, false);
    }

    private DocumentMetaDataProperty(Class<?> javaClass, String odataPropertyName, boolean id) {
        this(javaClass, odataPropertyName, odataPropertyName, null, id);
    }

    private DocumentMetaDataProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName,
                                    FullQualifiedName fullQualifiedName, boolean id) {
        this.entityProperty = EntityProperty.builder().javaClass(javaClass).odataPropertyName(odataPropertyName)
                .entityPropertyName(entityPropertyName).fullQualifiedName(fullQualifiedName).id(id).build();
    }

    public EntityProperty getEntityProperty() {
        return entityProperty;
    }

}
