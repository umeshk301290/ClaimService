package com.prgx.workbench.claim.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExceptionDetailMetadata {
	private AttachmentType attachmentType;
	private String tabName;
	private Map<String,String> exceptionDetailDataType;

}
