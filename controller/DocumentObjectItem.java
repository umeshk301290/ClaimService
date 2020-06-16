package com.prgx.workbench.claim.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DocumentObjectItem implements Serializable {

	private static final long serialVersionUID = 2898882399590888344L;

	private String documentId;

	@JsonIgnore
	private String objectKey;

	private String originalFileName;

	private String filePath;

}
