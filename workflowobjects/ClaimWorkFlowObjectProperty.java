package com.prgx.workbench.claim.workflowobjects;

import java.util.Date;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import com.prgx.workbench.core.odata.entity.provider.EntityProperty;
import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;

public enum ClaimWorkFlowObjectProperty implements PropertyProvider {

    ID(Long.class, "id", true),
    CLAIM_ID(Long.class, "claimId"),
    PROCESS_INSTANCE_ID(String.class, "processInstanceId"),
    WORK_FLOW_STATUS(WorkFlowStatusType.class, "workFlowStatus"),
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
    private ClaimWorkFlowObjectProperty(Class<?> javaClass, String odataPropertyName) {
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
    private ClaimWorkFlowObjectProperty(Class<?> javaClass, String odataPropertyName, boolean id) {
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
    private ClaimWorkFlowObjectProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName) {
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
    private ClaimWorkFlowObjectProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName, boolean id) {
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
    private ClaimWorkFlowObjectProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName,
            FullQualifiedName fullQualifiedName) {
        this(javaClass, odataPropertyName, entityPropertyName, fullQualifiedName, false);
    }

    private ClaimWorkFlowObjectProperty(Class<?> javaClass, String odataPropertyName, String entityPropertyName, FullQualifiedName fullQualifiedName,
            boolean id) {
        this.entityProperty = EntityProperty.builder().javaClass(javaClass).odataPropertyName(odataPropertyName)
                .entityPropertyName(entityPropertyName).fullQualifiedName(fullQualifiedName).id(id).build();
    }

    public EntityProperty getEntityProperty() {
        return entityProperty;
    }
    
    
}
