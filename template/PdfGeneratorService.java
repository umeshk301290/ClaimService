package com.prgx.workbench.claim.template;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.prgx.workbench.core.restclient.config.RestClientProvider;
import com.prgx.workbench.exception.ApiRuntimeException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PdfGeneratorService {

	@Autowired
	private RestClientProvider restClientProvider;

	@Value("${claim.template.pdf.upload.url}")
	private String uploadUrl;

	public ResponseEntity<Resource> generatePdf(InputStream inputStream, Long claimId)
			throws IOException {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = restClientProvider.getRestTemplate();
		ResponseEntity<Resource> response = null;
		HttpHeaders headers = new HttpHeaders();
		String fileName = claimId+"."+ClaimTemplateConstant.HTML;
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
		ContentDisposition contentDisposition = ContentDisposition.builder("form-data").name(ClaimTemplateConstant.file).filename(fileName)
				.build();
		fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
		byte[] bytes = IOUtils.toByteArray(inputStream);
		log.info("input stream generated is {} ", inputStream);
		log.info("byte array generated is {} and length is {}", bytes, bytes.length);
		HttpEntity<byte[]> fileEntity = new HttpEntity<>(bytes, fileMap);
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", fileEntity);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		try {
		response = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity,
				Resource.class);
		log.info("response from pdf file is {} ", response.getBody());

		}
		catch(HttpServerErrorException e) {
			log.error("error in generating pdf",e);
			throw new ApiRuntimeException(TemplateServiceException.PDF_GENERATION_ERROR,new Object[] {},e) ;
		}

		return response;
	}

}
