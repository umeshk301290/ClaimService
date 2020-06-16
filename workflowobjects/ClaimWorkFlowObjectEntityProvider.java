package com.prgx.workbench.claim.workflowobjects;


import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prgx.workbench.claim.workflow.definition.WorkflowEntityProvider;
import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.core.odata.entity.provider.impl.AbstractEntityProvider;
import com.prgx.workbench.core.odata.spring.OdataProperties;

@Component
public class ClaimWorkFlowObjectEntityProvider extends AbstractEntityProvider {

    public static final String ET_NAME = "WorkFlowObject";
    public static final String ES_NAME = "workflowobjects";
    public static final String WORKFLOW = "workflow";


    @Autowired
    private OdataProperties odataProperties;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------


   @Override
    public CsdlEntityType createEntityType() {

        List<CsdlNavigationProperty> navigationProperties = new ArrayList<>();

        
        CsdlNavigationProperty workflowProperty = new CsdlNavigationProperty().setName(WORKFLOW)
       .setType(getFullQualifiedName(WorkflowEntityProvider.ET_NAME)).setCollection(false);
        navigationProperties.add(workflowProperty);
        return super.createEntityType().setNavigationProperties(navigationProperties);
    }

        @Override
    public PropertyProvider[] getPropertyProviders() {
        return ClaimWorkFlowObjectProperty.values();
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
