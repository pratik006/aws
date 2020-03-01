package com.prapps.aws.email.rest;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.MessageRequest;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private AmazonS3Client amazonClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cloud.aws.end-point}")
    private String sqsEndpoint;
    @Value("${cloud.aws.email.bucketName}")
    private String bucketName;

    @PostMapping("/send")
    public void sendEmail(@RequestBody MessageRequest messageRequest) throws IOException {
        //queueMessagingTemplate.convertAndSend(sqsEndpoint, messageRequest);
        messageRequest.setTimestamp(LocalDateTime.now().toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/json");
        objectMapper.writeValue(baos, messageRequest);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        this.amazonClient.putObject(bucketName,
                String.valueOf(messageRequest.hashCode()),
                bais,
                objectMetadata);
    }
}
