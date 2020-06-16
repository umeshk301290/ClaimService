package com.prgx.workbench.claim.template;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.prgx.workbench.claim.ClaimSource;
import com.prgx.workbench.core.jpa.entity.BaseAuditableDeleteableEntity;
import com.prgx.workbench.odata.jpa.entity.OdataDeleteableEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "claimtemplatemapping")
public class ClaimTemplateMapping extends BaseAuditableDeleteableEntity implements OdataDeleteableEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "mappingcolumnlabel")
	private String mappingColumnLabel;

	@Column(name = "mappingexceptionfield")
	private String mappingExceptionField;

	@Column(name = "categeory")
	@Enumerated(EnumType.STRING)
	private ClaimTemplateCategeory claimTemplateCategeory;
	
	@Column(name = "source")
	@Enumerated(EnumType.STRING)
	private ClaimSource claimTemplateSource;
	
	

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		ClaimTemplateMapping template = (ClaimTemplateMapping) o;

		return new EqualsBuilder().appendSuper(super.equals(o)).append(getId(), template.getId())
				.append(getClaimTemplateCategeory(), template.getClaimTemplateCategeory())
				.append(getMappingColumnLabel(), template.getMappingColumnLabel())
				.append(getMappingExceptionField(), template.getMappingExceptionField()).append(getClaimTemplateSource(),template.getClaimTemplateSource()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(getId()).append(getClaimTemplateCategeory())
				.append(getMappingColumnLabel()).append(getMappingExceptionField()).append(getClaimTemplateSource()).toHashCode();
	}

}
