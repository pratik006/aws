package com.prapps.aws.email.process.lambda;

import static org.mockito.Mockito.mock;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

public class TestS3NotificationEvtRequestHandler {

    S3NotificationEvtRequestHandler handler = new S3NotificationEvtRequestHandler();

    @Test
    public void testHandleRequest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        S3EventNotification evt = objectMapper.readValue(new FileInputStream("src/test/resources/SampleS3Notification.json"), S3EventNotification.class);
        Context ctx = mock(Context.class);
        handler.handleRequest(evt, ctx);
    }
}
