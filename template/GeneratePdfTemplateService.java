package com.prgx.workbench.claim.template;

import static com.prgx.workbench.exception.message.GenericMessageCode.MSG_ERR_SYS_INTERNAL_ERROR;

import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import com.prgx.workbench.claim.Claim;
import com.prgx.workbench.claim.ClaimSource;
import com.prgx.workbench.claim.controller.AttachmentContextUtil;
import com.prgx.workbench.claim.controller.AttachmentController;
import com.prgx.workbench.claim.controller.AttachmentReaderService;
import com.prgx.workbench.claim.controller.AttachmentType;
import com.prgx.workbench.claim.controller.DocumentObjectBean;
import com.prgx.workbench.claim.controller.ExceptionDetailMetadata;
import com.prgx.workbench.core.objectattachment.config.AttachmentProperties;
import com.prgx.workbench.core.objectattachment.handler.AttachmentRequestHandler;
import com.prgx.workbench.core.objectattachment.model.AttachmentContext;
import com.prgx.workbench.exception.ApiRuntimeException;
import com.prgx.workbench.minio.readcsv.MinioCsvConstant;
import com.prgx.workbench.minio.readcsv.MinioCsvSelectService;
import freemarker.core.InvalidReferenceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeneratePdfTemplateService {

	@Autowired
	private AttachmentProperties attachmentProperties;

	@Value("${endpoint.objectstore.document}")
	private String urlServiceObjectstore;

	@Autowired
	private AttachmentRequestHandler attachmentRequestHandler;

	@Autowired
	private AttachmentReaderService attachmentReaderService;

	@Autowired
	private PdfGeneratorService templatePdfGenerator;

	@Autowired
	ClaimTemplateMappingRepository tempMappingRepo;

	@Autowired
	AttachmentController attachmentController;

	@Autowired
	MinioCsvSelectService minioCsvSelectService;

	public Resource storeTemplateData(Claim claim, String clientCode, String documentId) throws Exception {
		Resource claimInputStream = null;
		List<Map<String, Object>> csvList = prepareCsvData(claim, clientCode);
		ClaimTemplateDetails claimDetails = prepareClaimInformation(claim, csvList);
		claimInputStream = prepareClaimTemplate(claimDetails, clientCode, documentId, claim.getId());

		return claimInputStream;

	}

	public Resource prepareClaimTemplate(ClaimTemplateDetails claimDetails, String clientCode, String documentId,
			Long claimId) throws TemplateException, IOException {
		// TODO Auto-generated method stub
		InputStream inputStream = null;
		String claimContentHtml = null;
		ResponseEntity<Resource> metaData = null;
		BufferedReader reader = null;
		String templateString;
		StringBuilder builder = new StringBuilder();

		try {
			Resource resource = attachmentReaderService.getDocumentFromObjectstore(clientCode, documentId);
			InputStream str = resource.getInputStream();
			reader = new BufferedReader(new InputStreamReader(str));
			while ((templateString = reader.readLine()) != null) {
				builder.append(templateString);
			}
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
			Template claimDetailsTemplate = new Template(ClaimTemplateConstant.claimtemplatedetails,
					new StringReader(builder.toString()), cfg);
			claimContentHtml = FreeMarkerTemplateUtils.processTemplateIntoString(claimDetailsTemplate, claimDetails);
			log.info("templated generated in html format is {}", claimContentHtml);
			inputStream = new ByteArrayInputStream(claimContentHtml.getBytes(Charset.forName("UTF-8")));
			metaData = templatePdfGenerator.generatePdf(inputStream, claimId);

		} catch (InvalidReferenceException ex) {
			log.error("Invalid reference exception caught", ex);

			throw new ApiRuntimeException(TemplateServiceException.TEMPLATE_INPUT_MISMATCH_EXCEPTION, new Object[] {},
					ex);

		} finally {
			if (Objects.nonNull(reader)) {
				reader.close();
			}
		}

		// TODO Auto-generated method stub
		return metaData.getBody();

	}

	public List<Map<String, Object>> prepareCsvData(Claim claim, String clientCode) throws Exception {
		// TODO Auto-generated method stub
		Map<String, Object> values = null;
		List<String> minioContents = readFromMinio(claim, clientCode);

		if (Objects.nonNull(minioContents)) {
			ResponseEntity<List<DocumentObjectBean>> res = getClaimResponse(claim, clientCode);

			if (Objects.nonNull(res.getBody()) && res.getBody().size() > 0
					&& Objects.nonNull(res.getBody().get(0).getDocumentId())) {
				ExceptionDetailMetadata metadata = attachmentController
						.getDocumentMetataData(res.getBody().get(0).getDocumentId(), clientCode);

				List<Map<String, Object>> csvData = new ArrayList<Map<String, Object>>(minioContents.size() - 1);
				String[] headerInfo = minioContents.get(0).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
				int rowsInfo = (minioContents.size() - 1);
				for (int rowId = 1; rowId <= rowsInfo; rowId++) {
					String[] columnInfo = minioContents.get(rowId).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
					values = new LinkedHashMap<String, Object>(minioContents.size() - 1);
					for (int colId = 0; colId < headerInfo.length; colId++) {
						values.put(headerInfo[colId], attachmentReaderService.getData(
								metadata.getExceptionDetailDataType().get(headerInfo[colId]), columnInfo[colId]));
					}
					csvData.add(values);
				}

				return csvData;
			}
		}
		return null;
	}

	private ResponseEntity<List<DocumentObjectBean>> getClaimResponse(Claim claim, String clientCode) {
		// TODO Auto-generated method stub
		ResponseEntity<List<DocumentObjectBean>> res = null;

		try {
			String apiUrl = MessageFormat.format(urlServiceObjectstore, clientCode);
			HttpHeaders projectHeaders = new HttpHeaders();
			projectHeaders.setContentType(MediaType.APPLICATION_JSON);
			AttachmentContext claimAttachmentContext = AttachmentContextUtil.getAttachmentContext(claim.getId(), null,
					AttachmentType.CLAIM_DETAIL, clientCode, attachmentProperties.getDocumentAttachment().getContext());
			RequestEntity requestEntity = new RequestEntity(null, projectHeaders, HttpMethod.GET, URI.create(apiUrl));
			apiUrl = MessageFormat.format(urlServiceObjectstore, clientCode);
			res = attachmentRequestHandler.handleRequest(apiUrl, claimAttachmentContext, requestEntity,
					new ParameterizedTypeReference<List<DocumentObjectBean>>() {
					});
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new ApiRuntimeException(MSG_ERR_SYS_INTERNAL_ERROR,
					new Object[] { "Error in fetching claim details attachment." }, ex);

		}
		return res;
	}

	private ClaimDetails getClaimDetails(Map<String, Object> obj) {
		ClaimDetails claimDetails = new ClaimDetails();
		try {
			claimDetails = fetchClaimDetailsData(claimDetails, obj);
		} catch (Exception e) {
			log.error("error in fetching claim details data", e);
			throw new ApiRuntimeException(MSG_ERR_SYS_INTERNAL_ERROR,
					new Object[] { "error in fetching claim details data." }, e);
		}
		return claimDetails;
	}

	private ClaimDetails fetchClaimDetailsData(ClaimDetails claimDetails, Map<String, Object> map)
			throws ParseException {

		DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
		DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
		callSetterMethod(claimDetails, ClaimTemplateConstant.compCd,
				String.valueOf(map.get(ClaimTemplateConstant.compCd)));
		callSetterMethod(claimDetails, ClaimTemplateConstant.vendorNumber,
				String.valueOf(map.get(ClaimTemplateConstant.vendorNumber)));
		callSetterMethod(claimDetails, ClaimTemplateConstant.vendorName,
				String.valueOf(map.get(ClaimTemplateConstant.vendorName)));
		callSetterMethod(claimDetails, ClaimTemplateConstant.poNumber,
				String.valueOf(map.get(ClaimTemplateConstant.poNumber)));
		callSetterMethod(claimDetails, ClaimTemplateConstant.invoiceNumber,
				String.valueOf(map.get(ClaimTemplateConstant.invoiceNumber)));
		if ((Objects.nonNull(map.get(ClaimTemplateConstant.invoiceDate)))
				&& !map.get(ClaimTemplateConstant.invoiceDate).equals(ClaimTemplateConstant.NULL)) {
			String dateInString = (String) map.get(ClaimTemplateConstant.invoiceDate);
			if (!dateInString.equalsIgnoreCase("") && !dateInString.equals("00:00.0")) {
				Date date = inputFormat.parse(dateInString);
				callSetterMethod(claimDetails, ClaimTemplateConstant.invoiceDate, outputFormat.format(date));
			} else {
				callSetterMethod(claimDetails, ClaimTemplateConstant.invoiceDate, ClaimTemplateConstant.Blank);

			}
		} else {
			callSetterMethod(claimDetails, ClaimTemplateConstant.invoiceDate,
					(String) map.get(ClaimTemplateConstant.invoiceDate));

		}

		callSetterMethod(claimDetails, ClaimTemplateConstant.paymentNumber,
				String.valueOf(map.get(ClaimTemplateConstant.paymentNumber)));
		if ((Objects.nonNull(map.get(ClaimTemplateConstant.paymentDate)))
				&& !map.get(ClaimTemplateConstant.paymentDate).equals(ClaimTemplateConstant.NULL)) {
			String dateInString = (String) map.get(ClaimTemplateConstant.paymentDate);
			if (!dateInString.equalsIgnoreCase("") && !dateInString.equals("00:00.0")) {
				Date date = inputFormat.parse(dateInString);
				callSetterMethod(claimDetails, ClaimTemplateConstant.paymentDate, outputFormat.format(date));
			} else {
				callSetterMethod(claimDetails, ClaimTemplateConstant.paymentDate, ClaimTemplateConstant.NULL);

			}
		} else {
			callSetterMethod(claimDetails, ClaimTemplateConstant.paymentDate,
					(String) map.get(ClaimTemplateConstant.paymentDate));

		}
		callSetterMethod(claimDetails, ClaimTemplateConstant.amount,
				String.valueOf(map.get(ClaimTemplateConstant.amount)));
		callSetterMethod(claimDetails, ClaimTemplateConstant.discount,
				String.valueOf(map.get(ClaimTemplateConstant.discount)));
		callSetterMethod(claimDetails, ClaimTemplateConstant.netAmount,
				String.valueOf(map.get(ClaimTemplateConstant.netAmount)));

		return claimDetails;

	}

	public List<String> readFromMinio(Claim claim, String clientCode) throws Exception {
		List<String> minioContents = null;
		ResponseEntity<List<DocumentObjectBean>> res = null;
		res = getClaimResponse(claim, clientCode);
		if (Objects.nonNull(res.getBody()) && res.getBody().size() > 0
				&& Objects.nonNull(res.getBody().get(0).getDocumentId())) {
			String bucket = res.getBody().get(0).getBucket();
			String path = res.getBody().get(0).getFilePath();
			String objectKey = res.getBody().get(0).getObjectKey();
			String format = MinioCsvConstant.CSV;
			String[] file = objectKey.split("\\.");
			if (claim.getSource().getValue().equalsIgnoreCase(ClaimSource.EXCEPTIONS.getValue())
					|| claim.getSource().getValue().equalsIgnoreCase(ClaimSource.STATEMENT_CREDITS.getValue())) {
				if (Objects.isNull(file) || !file[1].equalsIgnoreCase(MinioCsvConstant.CSV)) {
					throw new ApiRuntimeException(TemplateServiceException.CSV_NOT_FOUND_ERROR);
				}
			}
			if (Objects.nonNull(file[1]) && file[1].equalsIgnoreCase(MinioCsvConstant.CSV)) {
				Optional<List<ClaimTemplateMapping>> claimTemplateMappingList = null;
				if (claim.getSource().getValue().equalsIgnoreCase(ClaimSource.EXCEPTIONS.getValue())) {
					claimTemplateMappingList = tempMappingRepo
							.findByCategeoryAndSource(ClaimTemplateCategeory.CLAIMDETAILS, ClaimSource.EXCEPTIONS);
				} else if (claim.getSource().getValue().equalsIgnoreCase(ClaimSource.STATEMENT_CREDITS.getValue())) {
					claimTemplateMappingList = tempMappingRepo.findByCategeoryAndSource(
							ClaimTemplateCategeory.CLAIMDETAILS, ClaimSource.STATEMENT_CREDITS);
				}

				if (Objects.isNull(claimTemplateMappingList) || !claimTemplateMappingList.isPresent()) {

					throw new ApiRuntimeException(TemplateServiceException.TEMPLATE_MAPPING_NOT_FOUND_MESSAGE);
				} else {
					List<String> headerInfoList = new ArrayList<String>();

					claimTemplateMappingList.get().forEach(claimDetailsList -> {
						headerInfoList.add(claimDetailsList.getMappingExceptionField());
					});
					String headerInfoString = headerInfoList.stream().collect(Collectors.joining(","));
					String claimQuery = "select";
					for (String headerInfo : headerInfoList) {
						claimQuery = claimQuery.concat(" s.").concat(headerInfo).concat(",");
					}
					claimQuery = claimQuery.replaceAll(",$", "");
					claimQuery = claimQuery.concat(" from s3object s");
					minioContents = minioCsvSelectService.getCsvContent(bucket, path + objectKey, claimQuery, format,
							Boolean.FALSE);
					minioContents.add(0, headerInfoString);
				}

			} else {
				log.info("No Csv exception details exists for reading");
			}

		} else {
			log.info("No Csv exception details exists for reading");
		}

		return minioContents;
	}

	public ClaimTemplateDetails prepareClaimInformation(Claim claim, List<Map<String, Object>> csvList) {
		// TODO Auto-generated method stub
		ClaimTemplateDetails claimTemplateDetails = new ClaimTemplateDetails();
		ClaimInformation claimInformation = prepareClaim(claim);
		List<ClaimDetails> claimDetailsList = prepareClaimDetails(claim, csvList);
		claimTemplateDetails.setClaimInformation(claimInformation);
		claimTemplateDetails.setClaimDetails(claimDetailsList);
		return claimTemplateDetails;
	}

	private List<ClaimDetails> prepareClaimDetails(Claim claim, List<Map<String, Object>> csvList) {
		// TODO Auto-generated method stub
		if (Objects.nonNull(csvList) && csvList.size() > 0) {
			List<Map<String, Object>> objList = new ArrayList<Map<String, Object>>();
			List<ClaimDetails> claimDetailsList = new ArrayList<ClaimDetails>();
			ClaimSource source = null;
			if (claim.getSource().getValue().equalsIgnoreCase(ClaimSource.EXCEPTIONS.getValue())) {
				source = ClaimSource.EXCEPTIONS;
			} else if (claim.getSource().getValue().equalsIgnoreCase(ClaimSource.STATEMENT_CREDITS.getValue())) {
				source = ClaimSource.STATEMENT_CREDITS;
			}
			Optional<List<ClaimTemplateMapping>> claimTemplateMappingList = tempMappingRepo
					.findByCategeoryAndSource(ClaimTemplateCategeory.CLAIMDETAILS, source);

			csvList.forEach(csv -> {

				Map<String, Object> templateMap = new HashMap<String, Object>();
				claimTemplateMappingList.get().forEach(templateMapping -> {
					templateMap.put(templateMapping.getMappingColumnLabel(),
							csv.get(templateMapping.getMappingExceptionField()));

				});
				objList.add(templateMap);
			});
			objList.forEach(obj -> {
				ClaimDetails schedule;

				schedule = getClaimDetails(obj);
				claimDetailsList.add(schedule);

			});
			return claimDetailsList;

		}
		return null;
	}

	private ClaimInformation prepareClaim(Claim claim) {
		// TODO Auto-generated method stub
		ClaimInformation claimInformation = new ClaimInformation();
		Optional<List<ClaimTemplateMapping>> claimTemplateMappingList = tempMappingRepo
				.findByCategeory(ClaimTemplateCategeory.CLAIM);
		;
		if (!claimTemplateMappingList.isPresent()) {
			throw new ApiRuntimeException(TemplateServiceException.TEMPLATE_MAPPING_NOT_FOUND_MESSAGE);
		} else {
			List<String> mappingLabels = claimTemplateMappingList.get().stream().filter(
					claimTemplateMapping -> StringUtils.isNotBlank(claimTemplateMapping.getMappingExceptionField()))
					.map(ClaimTemplateMapping::getMappingExceptionField).collect(Collectors.toList());
			mappingLabels.stream().forEach(field -> {
				try {

					PropertyDescriptor pd = new PropertyDescriptor(field, Claim.class);
					Method getter = pd.getReadMethod();
					Object fieldValue = getter.invoke(claim);
					callSetterMethod(claimInformation, field, String.valueOf(fieldValue));

				} catch (Exception e) {
					log.error("unable to parse methods using reflection", e);
					throw new ApiRuntimeException(MSG_ERR_SYS_INTERNAL_ERROR, new Object[] {}, e);

				}
			});
			if (Objects.nonNull(claim.getClaimDate())) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				callSetterMethod(claimInformation, ClaimTemplateConstant.claimDate,
						dateFormat.format(claim.getClaimDate()));

			} else {
				callSetterMethod(claimInformation, ClaimTemplateConstant.claimDate, ClaimTemplateConstant.Blank);

			}

			callSetterMethod(claimInformation, ClaimTemplateConstant.creditReference, ClaimTemplateConstant.Blank);
			callSetterMethod(claimInformation, ClaimTemplateConstant.creditReferenceDate, ClaimTemplateConstant.Blank);
			callSetterMethod(claimInformation, ClaimTemplateConstant.invoiceNumber, ClaimTemplateConstant.Blank);
			callSetterMethod(claimInformation, ClaimTemplateConstant.invoiceDate, ClaimTemplateConstant.Blank);
			callSetterMethod(claimInformation, ClaimTemplateConstant.poNumber, ClaimTemplateConstant.Blank);
			callSetterMethod(claimInformation, ClaimTemplateConstant.paymentNumber, ClaimTemplateConstant.Blank);
			callSetterMethod(claimInformation, ClaimTemplateConstant.paymentDate, ClaimTemplateConstant.Blank);
			callSetterMethod(claimInformation, ClaimTemplateConstant.summaryType, ClaimTemplateConstant.Audit);

			if (Objects.nonNull(claim.dynamicColumnNames())) {
				Map<String, Object> dynamicColumnMap = new LinkedHashMap<String, Object>();
				Collection<String> col = claim.dynamicColumnNames();
				col.stream().forEach(columnName -> {
					dynamicColumnMap.put(columnName, claim.getDynamicColumnValue(columnName));
				});
				claimInformation.setDynamicColumnValues(dynamicColumnMap);

			}
		}
		return claimInformation;
	}

	public void callSetterMethod(Object obj, String fieldName, Object value) {

		PropertyDescriptor pd;

		try {
			pd = new PropertyDescriptor(fieldName, obj.getClass());
			if (Objects.isNull(value) || value.equals((ClaimTemplateConstant.NULL))) {
				pd.getWriteMethod().invoke(obj, ClaimTemplateConstant.Blank);
			} else {
				pd.getWriteMethod().invoke(obj, value);

			}
		} catch (Exception e) {
			log.error("error in method conversion from property descriptor", e);
			throw new ApiRuntimeException(MSG_ERR_SYS_INTERNAL_ERROR,
					new Object[] { "error in method conversion from property descriptor" }, e);
		}

	}

}
