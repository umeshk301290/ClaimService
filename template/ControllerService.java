package com.prgx.workbench.claim.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="{clientCode}")
public class ControllerService {
	
@Autowired
GeneratePdfTemplate pdfTemplate;
	
@GetMapping(value = "generatetemplate/{claimId}/{documentId}")
public Resource getResource(@PathVariable("clientCode") String clientCode, @PathVariable("claimId") Long claimId,@PathVariable("documentId") String documentId) throws Exception {
	Resource resource = pdfTemplate.generateTemplatePdf(clientCode, claimId, documentId);
	return resource;
	
	
	
}

}
