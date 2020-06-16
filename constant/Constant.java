package com.prgx.workbench.claim.constant;

public class Constant {

	public static final String APP_VENDOR_ID = "appVendorId";
	public static final String OBJECT_TYPE = "objectType";
	public static final String PROJECT_OBJECT_TYPE = "project";
	public static final String ADHOC_SOURCE = "Adhoc";

	public static final String CAMUNDA_STRING_DATA_TYPE = "String";
	public static final String CAMUNDA_DATE_DATA_TYPE = "Date";
	public static final String CAMUNDA_DOUBLE_DATA_TYPE = "Double";

	public static final String CAMUNDA_AP_WORKFLOW = "AP";
	public static final String CAMUNDA_VENDOR_ONLY_WORKFLOW = "Vendor Only";
	public static final String RETAIL_SERVICE_LINE= "Retail";


	public static final String CAMUNDA_PROCESS_DEFINITION_KEY = "Claims_Workflow";

	public static final String CLAIMS_ENTITY_NAME = "Claim";

	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'";

	public static final String CREATED_DATE = "createdDate";
	public static final String CREATED_BY = "createdBy";
	public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
	public static final String Last_Modified_By = "lastModifiedBy";
	public static final String Id = "id";
	public static final String CLAIM_DATE = "claimDate";

	public static final String WORKFLOW_KEY_NAME = "workflow";

	public static final String STAGE_TASK_VARIABLE = "name";
	public static final String SERVICELINE_ID_VARIABLE = "serviceLineId";
	public static final String TASK_ID_TASK_VARIABLE = "id";
	public static final String ASSIGNEE_TASK_VARIABLE = "assignee";
	public static final String USER_ACTION_INPUT_VARIABLE = "userAction";
	public static final String USER_ACTION_ID_INPUT_VARIABLE = "userActionId";
	public static final String CAMUNDA_ACTION_INPUT_VARIABLE = "camundaAction";
	public static final String CLAIM_ENTITY_STATUS_FIELD = "status";
	public static final String CLAIM_ENTITY_STATUS_ID_FIELD = "statusId";
	public static final String CLAIM_ENTITY_DISPOSITION_ID_FIELD = "dispositionId";
	public static final String CLAIM_ENTITY_DISPOSITION_FIELD = "disposition";

	public static final String TEMPLATE_NAME_FIELD="templateConfigName";
	public static final String TEMPLATE_ACTIVE_DATE_FIELD="activeDate";
	public static final String TEMPLATE_INACTIVE_DATE_FIELD="inactiveDate";
	public static final String TEMPLATE_STATUS="status";
	public static final String VARIABLE="variables";
	public static final String CAMUNDA_APPROVAL_COMMENT_VARIABLE = "approvalNotes" ;
	
	public static final String FILEMETADATA="fileMetadata";
	public static final String CLAIM_SOURCE="source";
	public static final String CANCEL_ADJUSTMENT_TYPE_ID = "5E682C86-06F8-4CF1-8C16-D61330B85687" ;

	public static final String WORKFLOW_COMPLETE="Completed";
	public static final String WORKFLOW_INPROGRESS="in_progress";
	public static final String WORKFLOW_REJECTED="Rejected";

	private Constant() {
	}
}
