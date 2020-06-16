package com.prgx.workbench.claim.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.core.odata.entity.provider.impl.AbstractEntityProvider;
import com.prgx.workbench.core.odata.spring.OdataProperties;

@Component
public class ClaimTemplateMappingEntityProvider extends AbstractEntityProvider {

    public static final String ET_NAME = "ClaimTemplateMapping";
    public static final String ES_NAME = "claimtemplatemappings";

    @Autowired
    private OdataProperties odataProperties;

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------



        @Override
    public PropertyProvider[] getPropertyProviders() {
        return ClaimTemplateMappingProperty.values();
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
