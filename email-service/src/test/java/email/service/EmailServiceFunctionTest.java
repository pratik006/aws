package email.service;

import com.prapps.aws.email.EmailAccount;
import com.prapps.aws.email.EmailMessage;
import com.prapps.aws.email.MessageRequest;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@MicronautTest
public class EmailServiceFunctionTest {

    @Inject
    EmailServiceClient client;

    //@Test
    public void testFunction() throws Exception {
    	MessageRequest body = new MessageRequest();
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
        body.setEmailAccount(emailAccount);

        assertEquals("Success", client.apply(body).blockingGet());
    }
}
