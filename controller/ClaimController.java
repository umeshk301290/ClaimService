package com.prgx.workbench.claim.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgx.workbench.claim.Claim;
import com.prgx.workbench.claim.ClaimService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("{clientCode}/claims")
public class ClaimController {

	@Autowired
	private ClaimService claimService;

	@SuppressWarnings("unchecked")
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createClaim(
			@PathVariable("clientCode")
					String clientCode,
			@RequestParam("claimHeader")
					String payload,
			@RequestParam(value = "file", required = false)
					MultipartFile[] filesParts,
			@RequestHeader
					HttpHeaders httpHeaders) throws IOException {
		Map<String,Object> claimPayload = null;

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			final DateFormat df = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			claimPayload = objectMapper.readValue(payload, Map.class);
		} catch (IOException e) {
			return new ResponseEntity("Invalid Claim Header field", HttpStatus.BAD_REQUEST);
		}
				
		Claim claim = claimService.processClaim(clientCode, filesParts, httpHeaders, claimPayload);
		return new ResponseEntity<>(claim, HttpStatus.OK);
	}


	
}

