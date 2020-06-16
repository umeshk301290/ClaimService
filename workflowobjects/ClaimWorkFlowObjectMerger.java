package com.prgx.workbench.claim.workflowobjects;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import com.prgx.workbench.odata.jpa.mapper.Merger;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ClaimWorkFlowObjectMerger extends Merger<ClaimWorkFlowObject> {

	  void merge(@MappingTarget ClaimWorkFlowObject claimsWorkFlowObject, ClaimWorkFlowObject newClaimsWorkFlowObject);

}
