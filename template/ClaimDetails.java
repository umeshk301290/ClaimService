package com.prgx.workbench.claim.template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ClaimDetails {

	String compCd;
	String vendorNumber;
	String vendorName;
	String poNumber;
	String invoiceNumber;
	String invoiceDate;
	String paymentNumber;
	String paymentDate;
	String amount;
	String discount;
	String netAmount;

}
