package com.prgx.workbench.minio.readcsv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {
	
	@Autowired
	MinioConfig config;
	
	@Bean
	AmazonS3 amazonS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTP))
				.withEndpointConfiguration(new EndpointConfiguration(
						String.join(":", config.getUrl(), config.getPort()),
						"default"))
				.build();
	}
}
