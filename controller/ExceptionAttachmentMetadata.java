package com.prgx.workbench.claim.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionAttachmentMetadata {
	private String fileName;
	private String aliasName;
	private Integer docType;
	private boolean claimPackage;
	private int fileOrder;
	private Integer visibilityLevel;
	private ObjectId objectId;
	private String objectValueId;
	private Long moduleId;
	private Long projectVendorId;
}
