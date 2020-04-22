package email.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.EmailMessage;
import com.prapps.aws.email.MimeMessageRequest;
import io.micronaut.context.annotation.Value;
import io.micronaut.function.FunctionBean;
import io.micronaut.function.executor.FunctionInitializer;
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
import java.util.function.Function;

@FunctionBean("email-service")
public class EmailServiceFunction extends FunctionInitializer implements Function<MimeMessageRequest, String> {
    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceFunction.class);
    @Value("${cloud.aws.email.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Inject
    S3MessageSender s3MessageSender;
    @Inject
    ObjectMapper objectMapper;

    @Override
    public String apply(MimeMessageRequest messageRequest) {
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

    /**
     * This main method allows running the function as a CLI application using: echo '{}' | java -jar function.jar 
     * where the argument to echo is the JSON to be parsed.
     */
    public static void main(String...args) throws IOException {
        EmailServiceFunction function = new EmailServiceFunction();
        function.run(args, (context)-> function.apply(context.get(MimeMessageRequest.class)));
    }    
}

