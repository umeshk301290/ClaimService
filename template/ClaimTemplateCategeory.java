package com.prgx.workbench.claim.template;

import com.prgx.workbench.core.odata.entity.provider.EnumProperty;

public enum ClaimTemplateCategeory implements EnumProperty {

	CLAIM("CLAIM"), CLAIMDETAILS("CLAIMDETAILS");

	String value;

	ClaimTemplateCategeory(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getEnumName() {
		return getValue();
	}
}
