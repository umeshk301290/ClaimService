package com.prgx.workbench.claim.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ServiceLineResponse {

	private String serviceLineName;
	private Long serviceLineId;
	private Date activeDate;
	private Date inactiveDate;

	public Date getActiveDate() {
		return this.activeDate == null ? null : new Date(this.activeDate.getTime());
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate == null ? null : new Date(activeDate.getTime());
	}

	public Date getInactiveDate() {
		return this.inactiveDate == null ? null : new Date(this.inactiveDate.getTime());
	}

	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate == null ? null : new Date(inactiveDate.getTime());
	}
}
