package com.prgx.workbench.claim.template;

import java.util.Date;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.prgx.workbench.core.odata.entity.provider.EntityProperty;
import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.claim.ClaimSource;

public enum ClaimTemplateMappingProperty implements PropertyProvider {
	
    ID(Long.class, "id", true),
    MAPPING_COLUMN_LABEL(String.class, "mappingColumnLabel"),
    MAPPING_EXCEPTION_FIELD(String.class, "mappingExceptionField"),
    CLAIM_TEMPLATE_TYPE(ClaimTemplateCategeory.class, "claimTemplateCategeory"),
    CLAIM_TEMPLATE_SOURCE(ClaimSource.class, "claimTemplateSource"),
    CREATED_BY(String.class, "createdBy"),
    CREATED_DATE(Date.class, "createdDate"),
    LAST_MODIFIED_BY(String.class, "lastModifiedBy"),
    LAST_MODIFIED_DATE(Date.class, "lastModifiedDate");
   
    private final EntityProperty entityProperty;

    /**
     * Instantiates a new project type property.
     *
     * @param javaClass
     *            the java class
     * @param odataPropertyName
     *            the odata property name
     */
    private ClaimTemplateMappingProperty(Class<?> javaClass, String odataPropertyName) {
        this(javaClass, odataPropertyName, odataPropertyName);
    }

    /**
     * Instantiates a new  project type property.
     *
     * @param javaClass
     *            the java class
     * @param odataPropertyName
     *            the odata property name
     * @param id
     *            the id
     */
    private ClaimTemplateMappingProperty(Class<?> javaClass, String odataPropertyName, boolean id) {
        this(javaClass, odataPropertyName, odataPropertyName, id);
    }

    /**
     * Instantiates a new  project type property.
     *
     * @param javaClass
     *            the java class
     * @param odataPropertyName
     *            the odata property name
     * @param entityPropertyName
     *            the entity property name
     */
    private ClaimTemplateMappingProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName) {
        this(javaClass, odataPropertyName, entityPropertyName, null);
    }

    /**
     * Instantiates a new  project type property.
     *
     * @param javaClass
     *            the java class
     * @param odataPropertyName
     *            the odata property name
     * @param entityPropertyName
     *            the entity property name
     * @param id
     *            the id
     */
    private ClaimTemplateMappingProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName, boolean id) {
        this(javaClass, odataPropertyName, entityPropertyName, null, id);
    }

    /**
     * Instantiates a new  project type property.
     *
     * @param javaClass
     *            the java class
     * @param odataPropertyName
     *            the odata property name
     * @param entityPropertyName
     *            the entity property name
     * @param fullQualifiedName
     *            the full qualified name
     */
    private ClaimTemplateMappingProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName,
            FullQualifiedName fullQualifiedName) {
        this(javaClass, odataPropertyName, entityPropertyName, fullQualifiedName, false);
    }

    private ClaimTemplateMappingProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName, FullQualifiedName fullQualifiedName,
            boolean id) {
        this.entityProperty = EntityProperty.builder().javaClass(javaClass).odataPropertyName(odataPropertyName)
                .entityPropertyName(entityPropertyName).fullQualifiedName(fullQualifiedName).id(id).build();
    }

    public EntityProperty getEntityProperty() {
        return entityProperty;
    }
}
