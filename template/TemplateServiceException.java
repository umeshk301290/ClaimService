package com.prgx.workbench.claim.template;

import com.prgx.workbench.exception.message.MessageCode;

public enum TemplateServiceException implements MessageCode {

	CLAIM_ID_NOT_FOUND("claim.id.not.found.msg"), TEMPLATE_MAPPING_NOT_FOUND_MESSAGE(
			"template.mapping.not.found.msg"), TEMPLATE_INPUT_MISMATCH_EXCEPTION(
					"template.input.mismatch.exception"), PDF_GENERATION_ERROR(
							"pdf.generation.error"), CSV_NOT_FOUND_ERROR("csv.not.found.error");

	@Override
	public String get() {
		return this.code;
	}

	private String code;

	private TemplateServiceException(final String code) {
		this.code = code;
	}

}
