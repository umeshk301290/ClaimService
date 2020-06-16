package com.prgx.workbench.claim.document.metadata;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prgx.workbench.odata.jpa.repository.BaseRepository;

public interface DocumentMetaDataRepository extends BaseRepository<DocumentMetaData, UUID> {

    List<DocumentMetaData> findByCode(final String code);

    @Query(value = "FROM DocumentMetaData dmd WHERE dmd.documentMetaDataId IN (:documentIds)")
    List<DocumentMetaData> findByDocumentId(@Param("documentIds") List<UUID> documentIds);

}
