package com.prapps.aws.email.sqs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class SqsConfig {

    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.credentials.accessKey}")
    private String awsAccessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String awsSecretKey;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        return new MappingJackson2MessageConverter();
    }

    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.DEFAULT_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }
}
