package com.prgx.workbench.claim;

import com.prgx.workbench.core.odata.entity.provider.EnumProperty;

/**
 * The Enum ClaimSource.
 */
public enum ClaimSource implements EnumProperty {

	EXCEPTIONS("Exceptions"), STATEMENT_CREDITS("Statement Credits"), CLAIMS("Claims");

	private String value;

	private ClaimSource(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String getEnumName() {
		return getValue();
	}

	public static ClaimSource fromValue(String value) {
		ClaimSource claimSource = null;
		for (ClaimSource source : ClaimSource.values()) {
			if (source.getValue().equalsIgnoreCase(value)) {
				claimSource = source;
				break;
			}
		}
		return claimSource;
	}
}
