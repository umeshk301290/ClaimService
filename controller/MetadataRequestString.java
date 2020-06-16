package com.prgx.workbench.claim.controller;

import java.util.Map;

public  class MetadataRequestString {
	Map<String, String> fileMetadata;
	MetadataRequestString(){

	}

	public Map<String, String> getFileMetadata() {
		return fileMetadata;
	}

	public void setFileMetadata(Map<String,String> fileMetadata) {
		this.fileMetadata = fileMetadata;
	}
}
