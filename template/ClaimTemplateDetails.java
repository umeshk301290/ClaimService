package com.prgx.workbench.claim.template;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClaimTemplateDetails {

ClaimInformation claimInformation;
List<ClaimDetails> claimDetails;

}
