package com.prapps.aws.sqs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SentMessageListener {

    public static final String QUEUE_URL = "https://sqs.us-east-2.amazonaws.com/375170284425/EmailCompleteQueue";

    public static void main(String[] args) {
        String accessKey = System.getenv("AWS_ACCESS_KEY");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        assert(accessKey != null && secretKey != null);
        final AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonSQS amazonSQS = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2)
                .build();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest();
        receiveMessageRequest.setMaxNumberOfMessages(1);
        receiveMessageRequest.setWaitTimeSeconds(20);
        receiveMessageRequest.setQueueUrl(QUEUE_URL);

        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();
        deleteMessageRequest.setQueueUrl(QUEUE_URL);

        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(
                () -> {
                    System.out.println("Called.");
                    List<Message> messages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
                    System.out.println("messages.size() : "+messages.size());
                    messages.forEach(msg -> System.out.println("Message : "+msg.getBody()+" received."));
                    messages.forEach(msg -> {
                        deleteMessageRequest.setReceiptHandle(msg.getReceiptHandle());
                        amazonSQS.deleteMessage(deleteMessageRequest);
                    });
                }
                , 0, 1, TimeUnit.SECONDS
        );

    }
}