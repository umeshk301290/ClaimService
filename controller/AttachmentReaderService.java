package com.prgx.workbench.claim.controller;

import com.opencsv.CSVReader;
import com.prgx.workbench.claim.exception.ClaimServiceExceptionMessages;
import com.prgx.workbench.core.restclient.config.RestClientProvider;
import com.prgx.workbench.exception.ApiRuntimeException;
import com.prgx.workbench.odata.dataview.model.DataView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The Class AttachmentReaderService- fetch and read from store. Currently supports CSV only.
 */
@Service
@Slf4j
public class AttachmentReaderService {

    @Autowired
    private RestClientProvider restClientProvider;

    @Value("${prgx.attachment.document-attachment.service-url}")
    private String serviceUri;

    @Value("${default.document.readLimit}")
    private Integer defaultDocumentRecordReadLimit;

    @Value("${endpoint.objectstore.document}")
    private String objectStoreEndPoint ;

    private final String csvInputDateformat ="yyyy-MM-dd HH:mm:ss.SSS";

    private final String isoDateFormast="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    /**
     * Fetch claim supporting data.
     *
     * @param clientCode
     *            the client code
     * @param docType
     *            the doc type
     * @param documentId
     *            the document id
     * @param recordLimit
     *            the record limit
     * @return the collection
     */
    public Collection<DataView> fetchClaimSupportingData(String clientCode, DocumentType docType, String documentId,
            ExceptionDetailMetadata metadata, Integer recordLimit) {
        if (docType == null || docType == DocumentType.CSV) {
            try {
                return fetchDocumentRecords(clientCode, documentId, recordLimit, metadata);
            } catch (Exception e) {
                throwDocumentReadError(documentId, e);
            }
        }
        return Collections.emptyList();
    }

    private Collection<DataView> fetchDocumentRecords(String clientCode, String documentId, Integer recordLimit, ExceptionDetailMetadata metadata)
            throws IOException {
        Resource fileResource = getDocumentFromObjectstore(clientCode, documentId);
        Reader reader = null;
        List<String[]> allRows = null;
        CSVReader csvReader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(fileResource.getInputStream(), Charset.defaultCharset()));
            csvReader = new CSVReader(reader);
            if (recordLimit == null) {
                recordLimit = defaultDocumentRecordReadLimit;
            }
            log.info("Fetch Claim Supporting Data with documentId- {} and record limit- {}.", documentId, recordLimit);
            if (recordLimit == -1) {
                allRows = csvReader.readAll();
            } else {
                allRows = new ArrayList<>();
                for (int i = recordLimit; i >= 0; i--) {
                    String[] aRow = csvReader.readNext();
                    if (aRow == null) {
                        break;
                    }
                    allRows.add(aRow);
                }
            }

        } catch (IOException e) {
            throwDocumentReadError(documentId, e);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }

        log.info("Fetch successfully CSV rows from documentId- {} and record limit- {}.", documentId, recordLimit);
        return prepareDataViewsFromCsvData(allRows, metadata);
    }

    public Object getData(String dataType, String fieldValue) {
        if ("null".equalsIgnoreCase(fieldValue))
            return null;
        if(Objects.isNull(dataType))
            return fieldValue;
        Object value = null;

        switch (dataType.toLowerCase()) {
            case "decimal":
            case "money":
                value = new BigDecimal((String) fieldValue);
                break;
            case "long":
            case "double":
            case "bigdecimal":
            case "numeric":
                String numericValue = fieldValue;
                if (numericValue.contains(".")) {
                    value = new BigDecimal(numericValue);
                } else {
                    value = Integer.valueOf(numericValue);
                }
                break;
            case "datetime":
            case "date":
            case "localdate" :
            case "localdatetime":
            case "smalldatetime":
                SimpleDateFormat sdf = new SimpleDateFormat(csvInputDateformat);
                try {
                    Date date = sdf.parse(fieldValue);
                    SimpleDateFormat isoDateFormat = new SimpleDateFormat(isoDateFormast);
                    value =  isoDateFormat.format(date);
                } catch (ParseException e) {
                    value=fieldValue;
                }
                break;
            case "int":
            case "tinyint":
            case "integer":
            case "smallint":
                value = Integer.valueOf((String) fieldValue);
                break;
            case "bit":
                value = "true".equalsIgnoreCase(fieldValue);
                break;
            default:
                value = fieldValue;
                break;
        }
        return value;
    }

    private Collection<DataView> prepareDataViewsFromCsvData(List<String[]> allRows, ExceptionDetailMetadata metadata) {
        Collection<DataView> dataViews = new LinkedList<>();
        String[] headerRow = allRows.get(0);
        for (int rowId = 1; rowId < allRows.size(); rowId++) {
            Map<String, Object> values = new LinkedHashMap<>(allRows.size());
            for (int colId = 0; colId < headerRow.length; colId++) {
                values.put(headerRow[colId],  getData(metadata.getExceptionDetailDataType().get(headerRow[colId]), allRows.get(rowId)[colId]));
            }
            DataView aDataView = new DataView();
            aDataView.setValues(values);
            dataViews.add(aDataView);
        }
        return dataViews;
    }

    public Resource getDocumentFromObjectstore(String clientCode, String documentId) {
        boolean error = false;
        RestTemplate restTemplate = restClientProvider.getRestTemplate();
        StringBuilder urlBuilder= new StringBuilder();
        urlBuilder.append(serviceUri).append('/').append(clientCode).append('/').append(objectStoreEndPoint).append('/').append(documentId);
        String apiUrl = urlBuilder.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<MultiValueMap<Void, Object>> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Resource> response = null;
        try {
            response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, Resource.class);
            if (Objects.isNull(response)) {
                error = true;
            }
        } catch (HttpClientErrorException | ApiRuntimeException ex) {
            error = true;
        }

        if (error) {
            throwDocumentReadError(documentId, null);
        }
        return response.getBody();
    }

    public void throwDocumentReadError(String documentId, Exception cause) {
        log.error("Claim supporting Document read error for documentId - {}, type - {}", documentId, DocumentType.CSV);
        if (cause != null) {
            throw new ApiRuntimeException(ClaimServiceExceptionMessages.CLAIM_DOCUMENT_READ_ERROR,
                    new Object[] { documentId }, cause);
        } else {
            throw new ApiRuntimeException(ClaimServiceExceptionMessages.CLAIM_DOCUMENT_READ_ERROR,
                    new Object[] { documentId });
        }
    }

    public  enum DocumentType {
        CSV
    }

}
