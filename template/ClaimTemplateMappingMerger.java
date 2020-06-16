package com.prgx.workbench.claim.template;

import com.prgx.workbench.odata.jpa.mapper.Merger;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ClaimTemplateMappingMerger extends Merger<ClaimTemplateMapping> {

	  void merge(@MappingTarget ClaimTemplateMapping templateMapping, ClaimTemplateMapping newTemplateMapping);

}

