package com.prgx.workbench.claim.utils;

import com.prgx.workbench.claim.exception.ClaimServiceExceptionMessages;
import com.prgx.workbench.claim.workflow.consolidate.AuditProperties;
import com.prgx.workbench.claim.workflow.consolidate.AuditResponse;
import com.prgx.workbench.core.restclient.config.RestClientProvider;
import com.prgx.workbench.core.tenant.TenantInformationProvider;
import com.prgx.workbench.exception.ApiRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class ClaimUtils {

	@Autowired
	private AuditProperties auditProperties;

	@Autowired
	private TenantInformationProvider tenantInformationProvider;

	@Autowired
	private RestClientProvider restClientProvider;

	public Long getServiceLineIdFromProjectId(String projectId) {

		String projectApiUrl = MessageFormat.format(auditProperties.getProjectApiUrl(),
				tenantInformationProvider.getRawTenant(), projectId);
		log.info("projectApiUrl {}", projectApiUrl);

		RestTemplate restTemplate = restClientProvider.getRestTemplateWithToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<AuditResponse> auditResponse = restTemplate.exchange(projectApiUrl, HttpMethod.GET, httpEntity,
				AuditResponse.class);

		if (Objects.nonNull(auditResponse) && Objects.nonNull(auditResponse.getBody())
				&& Objects.nonNull(auditResponse.getBody().getAudit())) {
			return auditResponse.getBody().getAudit().getServiceLineId();
		} else
			throw new ApiRuntimeException(ClaimServiceExceptionMessages.SERVICE_LINE_NOT_FOUND_EXCEPTION_MESSAGE,
					new Object[] { projectId }, org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);

	}

	public ServiceLineResponse getServiceLineDetails(Long serviceLineId) {

		String projectApiUrl = MessageFormat.format(auditProperties.getServiceLineApiUrl(),
				tenantInformationProvider.getRawTenant(), String.valueOf(serviceLineId));
		log.info("serviceLine URl {}", projectApiUrl);

		RestTemplate restTemplate = restClientProvider.getRestTemplateWithToken();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);

		ResponseEntity<ServiceLineResponse> responseEntity = restTemplate.exchange(projectApiUrl, HttpMethod.GET, httpEntity,
				ServiceLineResponse.class);

		if (responseEntity.getStatusCode().is2xxSuccessful() && Objects.nonNull(responseEntity.getBody())) {
			return responseEntity.getBody();
		} else
			throw new ApiRuntimeException(ClaimServiceExceptionMessages.SERVICE_LINE_NOT_FOUND,
					new Object[] { serviceLineId }, org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR);

	}

}
