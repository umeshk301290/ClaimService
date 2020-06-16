package com.prgx.workbench.claim.utils.datadictionary;

import com.prgx.workbench.claim.workflow.consolidate.DataDictionaryProperties;
import com.prgx.workbench.core.data.dictionary.dataobjectfield.entity.DataObjectField;
import com.prgx.workbench.core.data.dictionary.dataobjectfield.entity.DataObjectFieldValue;
import com.prgx.workbench.core.data.dictionary.dataobjectfield.service.DataObjectFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ClaimDataDictionaryUtil {

	@Autowired
	private DataObjectFieldService dataObjectFieldService;

	@Autowired
	private DataDictionaryProperties dataDictionaryProperties;

	public UUID getStageValueIdFromDataDictionary(String stageName) {

		Optional<UUID> stageFieldId = null;
		DataObjectField dataObjectField = dataObjectFieldService
				.getEntity(UUID.fromString(dataDictionaryProperties.getStageFieldId()));
		List<DataObjectFieldValue> dataObjectFieldValues = dataObjectField.getDataObjectFieldValues();
		if (!dataObjectFieldValues.isEmpty()) {
			stageFieldId = dataObjectFieldValues.stream()
					.filter(dataObjectFieldValue -> dataObjectFieldValue.getValue().equalsIgnoreCase(stageName))
					.map(DataObjectFieldValue::getId).findFirst();
			if (stageFieldId.isPresent()) {
				return stageFieldId.get();
			}
		}

		return null;
	}

	public Optional<DataObjectFieldValue> getStatusValueIdFromDataDictionary(String statusName) {

		Optional<DataObjectFieldValue> statusField = null;
		DataObjectField dataObjectField = dataObjectFieldService
				.getEntity(UUID.fromString(dataDictionaryProperties.getStatusFieldId()));
		List<DataObjectFieldValue> dataObjectFieldValues = dataObjectField.getDataObjectFieldValues();
		if (!dataObjectFieldValues.isEmpty()) {
			statusField = dataObjectFieldValues.stream()
					.filter(dataObjectFieldValue -> dataObjectFieldValue.getValue().equalsIgnoreCase(statusName))
					.findFirst();
		}
		return statusField;
	}

	public Map<UUID, String> getDataDictionaryFieldValue(List<UUID> fieldValueIds, String objectFieldId) {

		Map<UUID, String> objectFieldMap = new HashMap<>(fieldValueIds.size());

		DataObjectField dataObjectField = dataObjectFieldService.getEntity(UUID.fromString(objectFieldId));
		List<DataObjectFieldValue> dataObjectFieldValues = dataObjectField.getDataObjectFieldValues();
		if (!dataObjectFieldValues.isEmpty()) {
			for (UUID objectFieldValueId : fieldValueIds) {
				Optional<String> objectFieldValueName = dataObjectFieldValues.stream()
						.filter(dataObjectFieldValue -> dataObjectFieldValue.getId().equals(objectFieldValueId))
						.map(DataObjectFieldValue::getValue).findFirst();
				objectFieldMap.put(objectFieldValueId,
						objectFieldValueName.isPresent() ? objectFieldValueName.get() : null);
			}

		}
		return objectFieldMap;
	}

}
