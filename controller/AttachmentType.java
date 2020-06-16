package com.prgx.workbench.claim.controller;

import com.prgx.workbench.core.odata.entity.provider.EnumProperty;

public enum AttachmentType implements EnumProperty {
	CLAIM_DETAIL("CLAIM_DETAIL"),
	SUPPORTING("SUPPORTING"),
	CLAIM_PACKAGE_COMPONENT_TEMPLATES("CLAIM_PACKAGE_COMPONENT_TEMPLATES");


	private final String type;

	AttachmentType(String type) {
		this.type = type;
	}

	public String getAttachmentType(){
		return  this.type;
	}

	@Override
	public String getEnumName() {
		return type;
	}
}
