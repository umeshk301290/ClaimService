package com.prgx.workbench.claim.document.metadata;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import com.prgx.workbench.odata.jpa.mapper.Merger;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface DocumentMetaDataMerger extends Merger<DocumentMetaData> {

	void merge(@MappingTarget DocumentMetaData documentMetaData, DocumentMetaData newDocumentMetaData);

}
