package com.prgx.workbench.claim.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * The Class DocumentObjectBean.
 */
@Getter
@Setter
@NoArgsConstructor
public class DocumentObjectBean {

	private String documentId;

	private String bucket;

	private String objectKey;

	private String fileName;

	private String filePath;

	private String context;

	private Map<String,Object> fileMetadata;



}
