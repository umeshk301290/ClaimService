package com.prgx.workbench.claim.template;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.prgx.workbench.claim.Claim;
import com.prgx.workbench.claim.ClaimRepository;
import com.prgx.workbench.exception.ApiRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GeneratePdfTemplate {

	@Autowired
	private GeneratePdfTemplateService generatePdfTemplateService;

	@Autowired
	private ClaimRepository repo;

	public Resource generateTemplatePdf(String clientCode, Long claimId, String documentId) throws Exception {
		log.info("processing template generation with claim id {}", claimId);
		Resource templateResource = null;
		Claim claim = null;
		Optional<Claim> op = repo.findById(claimId);
		if (op.isPresent()) {
			log.info("claim information found with claim id {}", claimId);
			claim = op.get();
		} else {
			throw new ApiRuntimeException(TemplateServiceException.CLAIM_ID_NOT_FOUND);
		}
		templateResource = generatePdfTemplateService.storeTemplateData(claim, clientCode, documentId);
		return templateResource;

	}
}
