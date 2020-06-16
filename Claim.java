package com.prgx.workbench.claim;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.prgx.workbench.claim.adjustments.ClaimAdjustmentEntity;
import com.prgx.workbench.claim.recovery.Recovery;
import com.prgx.workbench.core.currency.localization.annotation.ConvertedCurrencyValue;
import com.prgx.workbench.core.jpa.entity.BaseAuditableDeleteableEntity;
import com.prgx.workbench.core.objectattachment.model.AttachmentAbleEntity;
import com.prgx.workbench.core.objectattachment.model.AttachmentStatus;
import com.prgx.workbench.odata.jpa.entity.OdataDeleteableEntity;
import com.prgx.workbench.odata.jpa.entity.OdataDynamicColumnEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "claim")
public class Claim extends BaseAuditableDeleteableEntity
		implements OdataDeleteableEntity<Long>, OdataDynamicColumnEntity<Long>, AttachmentAbleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "projectid")
	private String projectId;

	@Column(name = "auditname")
	private String auditName;

	@Column(name = "auditproject")
	private String auditProject;

	@Column(name = "claimamount")
	private BigDecimal claimAmount;

	@Column(name = "claimcurrency")
	private String claimCurrency;

	@Column(name = "claimnumber")
	private String claimNumber;

	@Column(name = "clientclaimnumber")
	private String clientClaimNumber;

	@Column(name = "dispositionid")
	private String dispositionId;

	@Column(name = "disposition")
	private String disposition;

	@Column(name = "netclaimamount")
	private BigDecimal netClaimAmount;

	@Column(name = "stageid")
	private String stageId;

	@Column(name = "stage")
	private String stage;

	@Column(name = "statusid")
	private String statusId;

	@Column(name = "status")
	private String status;

	@Column(name = "vendorname")
	private String vendorName;

	@Column(name = "vendornumber")
	private String vendorNumber;

	@Column(name = "vendorid")
	private String vendorId;

	@Column(name = "rootcausedescription")
	private String rootCauseDescription;

	@Column(name = "rootcauseid")
	private String rootCauseId;

	@Column(name = "jsonvalue ")
	@Type(type = "prgx_json")
	private Map<String, Object> dynamicColumnValues;

	@Transient
	private Integer paddingLength = 7;

	@Column(name = "businessunit")
	private String businessUnit;

	@Column(name = "narrative")
	private String narrative;

	@Column(name = "problemtypeid")
	private String problemTypeId;

	@Column(name = "problemtype")
	private String problemType;

	@Column(name = "claimdate")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date claimDate = new Date();

	@Column(name = "exchangerate")
	private BigDecimal exchangeRate;

	@Column(name = "appvendorid")
	@Type(type = "uuid-char")
	private UUID appVendorId;

	@Column(name = "sourcefieldvalue")
	private String sourceFieldValue;

	@Column(name = "source")
	@Enumerated(EnumType.STRING)
	private ClaimSource source;

	@OneToMany(mappedBy = "claim", fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<ClaimAdjustmentEntity> claimAdjustments;

	@JsonManagedReference
	@OneToMany(mappedBy = "claim", fetch = FetchType.LAZY)
	private List<Recovery> recovery;

	@Transient
	private List<AttachmentStatus> attachmentStatus;

	@Column(name = "templateid")
	private Long templateId;

	@Transient
	@ConvertedCurrencyValue(currencyColumn = "localCurrencies")
	private LocalCurrencyClaim localCurrency;

	@ElementCollection
	@CollectionTable(name = "claimlocalcurrency", joinColumns = @JoinColumn(name = "id"))
	private List<LocalCurrencyClaim> localCurrencies;

	public Date getClaimDate() {
		return this.claimDate == null ? null : new Date(this.claimDate.getTime());
	}

	public void setClaimDate(Date claimDate) {
		this.claimDate = claimDate == null ? null : new Date(claimDate.getTime());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Claim claim = (Claim) o;

		return new EqualsBuilder().appendSuper(super.equals(o)).append(getId(), claim.getId())
				.append(getProjectId(), claim.getProjectId()).append(getAuditName(), claim.getAuditName())
				.append(getClaimAmount(), claim.getClaimAmount()).append(getClaimCurrency(), claim.getClaimCurrency())
				.append(getClaimNumber(), claim.getClaimNumber())
				.append(getClientClaimNumber(), claim.getClientClaimNumber())
				.append(getDispositionId(), claim.getDispositionId())
				.append(getNetClaimAmount(), claim.getNetClaimAmount()).append(getStageId(), claim.getStageId())
				.append(getStatusId(), claim.getStatusId()).append(getVendorNumber(), claim.getVendorNumber())
				.append(getVendorId(), claim.getVendorId()).append(getRootCauseId(), claim.getRootCauseId())
				.append(getBusinessUnit(), claim.getBusinessUnit()).append(getNarrative(), claim.getNarrative())
				.append(getProblemTypeId(), claim.getProblemTypeId()).append(getProblemType(), claim.getProblemType())
				.append(getClaimDate(), claim.getClaimDate()).append(getExchangeRate(), claim.getExchangeRate())
				.append(getAppVendorId(), claim.getAppVendorId()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getId()).append(getProjectId())
				.append(getAuditName()).append(getClaimAmount()).append(getClaimCurrency()).append(getClaimNumber())
				.append(getClientClaimNumber()).append(getDispositionId()).append(getNetClaimAmount())
				.append(getStageId()).append(getStatusId()).append(getVendorNumber()).append(getVendorId())
				.append(getRootCauseId()).append(getBusinessUnit()).append(getNarrative()).append(getProblemTypeId())
				.append(getProblemType()).append(getClaimDate()).append(getExchangeRate()).append(getAppVendorId())
				.toHashCode();
	}

	@Override
	public void setDynamicColumnValue(String entityPropertyName, Object fieldValue) {
		getDynamicColumnValues().put(entityPropertyName, fieldValue);
	}

	@Override
	public Object getDynamicColumnValue(String entityPropertyName) {
		return getDynamicColumnValues().get(entityPropertyName);
	}

	@Override
	public List<AttachmentStatus> getAttachmentStatus() {
		return attachmentStatus;
	}

	@Override
	public void setAttachmentStatus(List<AttachmentStatus> attachmentStatus) {
		this.attachmentStatus = attachmentStatus;
	}

	private Map<String, Object> getDynamicColumnValues() {
		if (Objects.isNull(dynamicColumnValues)) {
			dynamicColumnValues = new LinkedHashMap<>();
		}
		return dynamicColumnValues;
	}

	@Override
	public Collection<String> dynamicColumnNames() {
		return getDynamicColumnValues().keySet();
	}
}
