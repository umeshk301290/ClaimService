package com.prgx.workbench.claim.workflowobjects;

import com.prgx.workbench.core.odata.entity.provider.EnumProperty;

/**
 * The Enum ClaimWorkflowStatus.
 */
public enum WorkFlowStatusType implements EnumProperty {

    COMPLETED("completed"), IN_PROGRESS("in_progress"), REJECTED("rejected");

    private String value;

    private WorkFlowStatusType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getEnumName() {
        return getValue();
    }
}
