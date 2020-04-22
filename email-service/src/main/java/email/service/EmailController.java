package email.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.EmailMessage;
import com.prapps.aws.email.MimeMessageRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@Controller("/email")
public class EmailController {
    private static final Logger LOG = LoggerFactory.getLogger(EmailController.class);

    @Inject
    S3MessageSender s3MessageSender;
    @Inject
    ObjectMapper objectMapper;

    @Get("/")
    public String hello() {
        return "hello from email controller";
    }

    @Post("/send")
    public String sendEmail(@Body MimeMessageRequest messageRequest) {
        LOG.trace("messageRequest: "+messageRequest);
        try {
            EmailMessage emailMessage = messageRequest.getEmailMessage();
            MimeMessage message = new MimeMessage(Session.getInstance(new Properties()));
            message.setRecipients(Message.RecipientType.TO, messageRequest.getEmailMessage().getRecipients());
            message.setText(emailMessage.getTextBody());
            message.setSubject(emailMessage.getSubject());
            message.setFrom(messageRequest.getEmailAccount().getUsername());

            MimeMessageRequest newReq = new MimeMessageRequest();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            message.writeTo(baos);
            newReq.setMessageContent(baos.toByteArray());
            baos.close();
            newReq.setEmailAccount(messageRequest.getEmailAccount());
            baos = new ByteArrayOutputStream();
            objectMapper.writeValue(baos, newReq);
            s3MessageSender.addFile(String.valueOf(messageRequest.hashCode()), "application/json", baos.toByteArray());
            baos.close();
        } catch (IOException | MessagingException e) {
            e.printStackTrace();
            return "failure";
        }

        return "success";
    }
}
