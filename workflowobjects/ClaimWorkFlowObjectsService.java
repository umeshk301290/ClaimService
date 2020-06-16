package com.prgx.workbench.claim.workflowobjects;

import com.prgx.workbench.core.odata.expression.Expression;
import com.prgx.workbench.core.odata.expression.impl.BinaryExpression;
import com.prgx.workbench.core.odata.expression.impl.FieldNameExpression;
import com.prgx.workbench.core.odata.expression.impl.LiteralValueExpression;
import com.prgx.workbench.core.odata.expression.impl.operator.BinaryOperator;
import com.prgx.workbench.core.odata.filter.Filter;
import com.prgx.workbench.core.odata.model.Query;
import com.prgx.workbench.exception.ApiRuntimeException;
import com.prgx.workbench.exception.message.GenericMessageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prgx.workbench.core.odata.entity.provider.EntityProvider;
import com.prgx.workbench.odata.jpa.mapper.Merger;
import com.prgx.workbench.odata.jpa.mapper.impl.LongIdConverter;
import com.prgx.workbench.odata.jpa.service.impl.BaseDeletableService;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClaimWorkFlowObjectsService extends BaseDeletableService<ClaimWorkFlowObject, Long> {

	@Autowired
	private ClaimWorkFlowObjectEntityProvider claimWorkFlowObjectEntityProvider;

	@Autowired
	private ClaimWorkFlowObjectsRepository claimWorkFlowRepository;

	@Autowired
	private ClaimWorkFlowObjectMerger claimWorkFlowMerger;

	@Autowired
	private LongIdConverter idConverter;

	@Autowired
	private ClaimWorkFlowObjectMapper claimWorkFlowMapper;

	@Override
	protected Class<ClaimWorkFlowObject> getEntityClass() {
		return ClaimWorkFlowObject.class;
	}

	@Override
	protected ClaimWorkFlowObjectsRepository getRepository() {
		return claimWorkFlowRepository;
	}

	@Override
	public LongIdConverter getIdMapper() {
		return idConverter;
	}

	@Override
	public Merger<ClaimWorkFlowObject> getMerger() {
		return claimWorkFlowMerger;
	}

	@Override
	public ClaimWorkFlowObjectMapper getEntityMapper() {
		return claimWorkFlowMapper;
	}

	@Override
	public EntityProvider getEntityProvider() {
		return claimWorkFlowObjectEntityProvider;
	}

}
