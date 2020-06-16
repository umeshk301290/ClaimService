package com.prgx.workbench.claim.workflowobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.prgx.workbench.claim.workflow.definition.Workflow;
import com.prgx.workbench.core.jpa.entity.BaseAuditableDeleteableEntity;
import com.prgx.workbench.odata.jpa.entity.OdataDeleteableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
@Table(name = "workflowobject")
public class ClaimWorkFlowObject extends BaseAuditableDeleteableEntity implements OdataDeleteableEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "claimid")
	private Long claimId;

	@Column(name = "processinstanceid")
	private String processInstanceId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowid", referencedColumnName = "id")
	private Workflow workflow;

	@Column(name = "workflowstatus")
	@Enumerated(EnumType.STRING)
	private WorkFlowStatusType workFlowStatus;

	@Column(name = "locked")
	private Boolean locked = Boolean.FALSE;
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		ClaimWorkFlowObject claimWorkFlowObject = (ClaimWorkFlowObject) o;

		return new EqualsBuilder().appendSuper(super.equals(o)).append(getId(), claimWorkFlowObject.getId())
				.append(getProcessInstanceId(), claimWorkFlowObject.getProcessInstanceId())
				.append(getClaimId(), claimWorkFlowObject.getClaimId())
				.append(getWorkFlowStatus(), claimWorkFlowObject.getWorkFlowStatus())
				.append(getLocked(), claimWorkFlowObject.getLocked()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getId()).append(getClaimId())
				.append(getProcessInstanceId()).append(getWorkFlowStatus()).append(getLocked()).toHashCode();
	}



}
