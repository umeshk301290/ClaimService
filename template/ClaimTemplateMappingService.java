package com.prgx.workbench.claim.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.prgx.workbench.core.odata.entity.provider.EntityProvider;
import com.prgx.workbench.odata.jpa.mapper.Merger;
import com.prgx.workbench.odata.jpa.mapper.impl.LongIdConverter;
import com.prgx.workbench.odata.jpa.service.impl.BaseDeletableService;

@Component
public class ClaimTemplateMappingService extends BaseDeletableService<ClaimTemplateMapping, Long> {

	@Autowired
	private ClaimTemplateMappingEntityProvider templateEntityProvider;

	@Autowired
	private ClaimTemplateMappingRepository templateRepository;

	@Autowired
	private ClaimTemplateMappingMerger templateMerger;

	@Autowired
	private LongIdConverter idConverter;

	@Autowired
	private ClaimTemplateMappingConverter templateMapper;

	@Override
	protected Class<ClaimTemplateMapping> getEntityClass() {
		return ClaimTemplateMapping.class;
	}

	@Override
	protected ClaimTemplateMappingRepository getRepository() {
		return templateRepository;
	}

	@Override
	public LongIdConverter getIdMapper() {
		return idConverter;
	}

	@Override
	public Merger<ClaimTemplateMapping> getMerger() {
		return templateMerger;
	}

	@Override
	public ClaimTemplateMappingConverter getEntityMapper() {
		return templateMapper;
	}

	@Override
	public EntityProvider getEntityProvider() {
		return templateEntityProvider;
	}

}
