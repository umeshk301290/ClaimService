package com.prgx.workbench.claim.document.metadata;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.prgx.workbench.core.jpa.entity.BaseAuditableDeleteableEntity;
import com.prgx.workbench.odata.jpa.entity.OdataDeleteableEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name ="documentmetadata")
public class DocumentMetaData extends BaseAuditableDeleteableEntity implements OdataDeleteableEntity<UUID> {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID documentMetaDataId ;

    @Column(name = "projectid")
    private Long projectId ;

    @Type(type = "uuid-char")
    @Column(name="appvendorid")
    private UUID appVendorId;
    
    @Column(name = "projectvendorid")
    private Long projectVendorId ;

    @Column(name = "fileorder")
    private Long fileOrder;

    @Column(name = "moduleid")
    private Long moduleId;

    @Column(name = "documenttypeid")
    private Long documentTypeId;

    @Column(name = "documentname")
    private String documentName;

    @Column(name = "aliasfilename")
    private String aliasFileName;

    @Column(name = "documentstatus")
    private String documentStatus;

    @Column(name = "comment")
    private String comment;

    @Column(name = "claimpackage")
    private boolean claimPackage;

    @Column(name="objectid")
    private String objectId;

    @Column(name="objectvalueid")
    private String objectValueId;

    @Column(name = "visibilitylevel")
    private int visibilityLevel;

    @Column(name="code")
    private String code;

    @Column(name="sendfrom")
    private String sendFrom;
    
    @Override
    public void setId(UUID id) {
        this.documentMetaDataId = id;
    }

    @Override
    public UUID getId() {
        return documentMetaDataId;
    }
}
