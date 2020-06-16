package com.prgx.workbench.minio.readcsv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CSVInput;
import com.amazonaws.services.s3.model.CSVOutput;
import com.amazonaws.services.s3.model.CompressionType;
import com.amazonaws.services.s3.model.ExpressionType;
import com.amazonaws.services.s3.model.FileHeaderInfo;
import com.amazonaws.services.s3.model.InputSerialization;
import com.amazonaws.services.s3.model.OutputSerialization;
import com.amazonaws.services.s3.model.SelectObjectContentRequest;
import com.amazonaws.services.s3.model.SelectObjectContentResult;
import com.prgx.workbench.claim.exception.ClaimServiceExceptionMessages;
import com.prgx.workbench.exception.ApiRuntimeException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MinioCsvSelectService {
	
    @Autowired
	 AmazonS3 client;
	

	
	public List<String> getCsvContent(String bucket, String key,String query, String format,Boolean isheaderRequired) throws IOException{
		log.info("reading contents for csv file {} for query {}",key,query);
		List<String> list = new ArrayList<String>();
		SelectObjectContentRequest request = generateBaseCSVRequest(bucket, key, query, format,isheaderRequired);
		SelectObjectContentResult result = client.selectObjectContent(request);
		InputStream stream = result.getPayload().getRecordsInputStream();
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			reader.lines().forEach(line -> {
	        	  list.add(line);
	          });
		}
	
		
	return list;
	
	}
	
	public SelectObjectContentRequest generateBaseCSVRequest(String bucket, String key, String query, String output,Boolean isHeaderRequest) {
		validateFields(bucket,key,output);
		SelectObjectContentRequest request = new SelectObjectContentRequest();
		request.setBucketName(bucket);
		request.setKey(key);
		request.setExpression(query);
		request.setExpressionType(ExpressionType.SQL);
		String[] file = key.split("\\.");

		InputSerialization inputSerialization = new InputSerialization();
		
		CSVInput csvInput = new CSVInput();
		if(isHeaderRequest) {
		csvInput.setFileHeaderInfo(FileHeaderInfo.NONE);
		}
		else {
			csvInput.setFileHeaderInfo(FileHeaderInfo.USE);	

		}
		inputSerialization.setCsv(csvInput);
		if(file.length < 3) {
			inputSerialization.setCompressionType(CompressionType.NONE);
		} else if(file[2].equals("gz")) {
			inputSerialization.setCompressionType(CompressionType.GZIP);
		} else {
			inputSerialization.setCompressionType(CompressionType.BZIP2);
		}
		request.setInputSerialization(inputSerialization);

		OutputSerialization outputSerialization = new OutputSerialization();
			CSVOutput csvOutput = new CSVOutput();
			outputSerialization.setCsv(csvOutput);
			request.setOutputSerialization(outputSerialization);

		return request;
	}

	private void validateFields(String bucket, String key, String output) {
		// TODO Auto-generated method stub
		
		String[] file = key.split("\\.");
		if(Objects.isNull(file[1]) || !file[1].equalsIgnoreCase(MinioCsvConstant.CSV)) {
		throw new ApiRuntimeException(ClaimServiceExceptionMessages.INCORRECT_EXTENTION_FILE);
		}
		
		if(!output.equalsIgnoreCase(MinioCsvConstant.CSV)) {
		throw new ApiRuntimeException(ClaimServiceExceptionMessages.INCORRECT_EXTENTION_FILE);

		}

	}

	

}
