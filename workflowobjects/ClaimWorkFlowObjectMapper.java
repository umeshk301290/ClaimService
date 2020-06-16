package com.prgx.workbench.claim.workflowobjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prgx.workbench.claim.workflow.definition.WorkflowMapper;
import com.prgx.workbench.core.odata.entity.provider.PropertyProvider;
import com.prgx.workbench.odata.jpa.mapper.impl.BaseMapper;

@Component
public class ClaimWorkFlowObjectMapper extends BaseMapper<ClaimWorkFlowObject> {

	@Autowired
	private WorkflowMapper workFlowMapper;

	@Override
	protected BaseMapper<?> getMapper(String fieldName) {

		BaseMapper<?> baseMapper = null;

		if (ClaimWorkFlowObjectEntityProvider.WORKFLOW.equals(fieldName)) {
			return workFlowMapper;
		}
		return baseMapper;
	}


	@Override
	protected Class<ClaimWorkFlowObject> getObjectClass() {
		return ClaimWorkFlowObject.class;
	}

	@Override
	protected PropertyProvider[] getPropertyProviders() {
		return ClaimWorkFlowObjectProperty.values();
	}

}
