package com.prapps.aws.email.process.lambda;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.MimeMessageRequest;

import javax.mail.MessagingException;
import java.io.IOException;

public class S3NotificationEvtRequestHandler implements RequestHandler<S3EventNotification, String> {

    private ObjectMapper objectMapper = new ObjectMapper();
    private MimeEmailSender lambdaRequestHandler = new MimeEmailSender();

    public String handleRequest(S3EventNotification evt, Context context) {
        context.getLogger().log("Evt Notification Json: " + evt.toJson());
        String bucketName = evt.getRecords().get(0).getS3().getBucket().getName();
        String fileKey = evt.getRecords().get(0).getS3().getObject().getKey();
        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileKey);
        S3Object object = s3.getObject(getObjectRequest);
        S3ObjectInputStream is = object.getObjectContent();

        MimeMessageRequest messageRequest;
        try {
            context.getLogger().log("retrieved S3ObjectInputStream");
            messageRequest = objectMapper.readValue(is, MimeMessageRequest.class);
            context.getLogger().log("cast to messageRequest");
            lambdaRequestHandler.handleRequest(messageRequest, context);
            return "success";
        } catch (IOException | MessagingException e) {
            context.getLogger().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
