package email.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.*;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class EmailServiceFunctionTest {

    @Inject
    EmailServiceClient client;

    @Test
    public void testFunction() throws Exception {
    	/*MessageRequest body = new MessageRequest(MessageType.MIME);
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setFrom("ixtester01@ixemailtester.onmicrosoft.com");
        emailMessage.setRecipients("pioneerstest101@gmail.com");
        emailMessage.setSubject("Test Subject 7");
        emailMessage.setTextBody("Test body 7");
    	body.setEmailMessage(emailMessage);
        EmailAccount emailAccount = new EmailAccount();
        emailAccount.setHost("smtp.office365.com");
        emailAccount.setPassword("Pega@123!");
        emailAccount.setPort(587);
        emailAccount.setUsername("ixtester01@ixemailtester.onmicrosoft.com");
        body.setEmailAccount(emailAccount);*/
        ObjectMapper mapper = new ObjectMapper();
        MessageRequest request = mapper.readValue(
                new FileInputStream("src/test/resources/testdata/simple-msg-request.json"), MimeMessageRequest.class);
        assertEquals("Success", client.apply(request).blockingGet());
    }
}
