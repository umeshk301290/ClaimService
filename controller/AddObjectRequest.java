package com.prgx.workbench.claim.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class AddObjectRequest.
 */
@Data
public class AddObjectRequest implements Serializable {

	private static final long serialVersionUID = 3056010100540159323L;

	private List<DocumentObjectItem> documentItems;

	private LocalDateTime uploadDate;

	@JsonIgnore
	private String bucket;

	private String filePath;

	private Map<String, String> userMetadata;

	private Map<String,String> fileMetadata;

	public AddObjectRequest() {
		userMetadata = new HashMap<>();
		fileMetadata = new HashMap<>();
		documentItems = new ArrayList<>();
	}

	public void addObjectItem(String objectKey, String filePath, String originalFileName) {
		DocumentObjectItem documentItem = new DocumentObjectItem();
		documentItem.setObjectKey(objectKey);
		documentItem.setFilePath(filePath);
		documentItem.setOriginalFileName(originalFileName);
		documentItems.add(documentItem);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getDocumentItems()).append(getUploadDate()).append(getBucket())
				.append(getFilePath()).append(getUserMetadata()).build();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		AddObjectRequest that = (AddObjectRequest) o;

		return new EqualsBuilder().append(getDocumentItems(), that.getDocumentItems())
				.append(getUploadDate(), that.getUploadDate()).append(getBucket(), that.getBucket())
				.append(getFilePath(), that.getFilePath()).append(getUserMetadata(), that.getUserMetadata())
				.append(getFileMetadata(), that.getFileMetadata()).isEquals();
	}
}
