package com.prgx.workbench.claim.template;


import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClaimInformation {

	String auditName;
	String businessUnit;
	String claimNumber;
	String claimDate;
	String netClaimAmount;
	String claimCurrency;
	String vendorName;
	String problemType;
	String creditReference;
	String creditReferenceDate;
	String rootCauseDescription;
	String vendorNumber;
	String invoiceNumber;
	String invoiceDate;
	String paymentNumber;
	String paymentDate;
	String poNumber;
	String narrative;
	String summaryType;
	String auditProject;
	String clientClaimNumber;
	String disposition;
	String stage;
	String status;
	String rootCauseId;
	String projectId;
	String dispositionId;
	String stageId;
	String statusId;
	String vendorId;
	String problemTypeId;
	String exchangeRate;
	String appVendorId;
	String templateId;
	Map<String,Object> dynamicColumnValues;

}
