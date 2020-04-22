package com.prapps.aws.email.process.lambda;

import static com.prapps.aws.email.process.lambda.EmailAccountSample.*;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prapps.aws.email.EmailAccount;
import com.prapps.aws.email.MessageRequest;
import com.prapps.aws.email.MessageType;
import com.prapps.aws.email.MimeMessageRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class TestMimeEmailSender {

    private static final Log LOG = LogFactory.getLog(TestMimeEmailSender.class);
    MimeEmailSender sender = new MimeEmailSender();

    private Session getSession(EmailAccountSample account) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", ixtester01.getSmtp().getHost());
        properties.put("mail.smtp.port", String.valueOf(ixtester01.getSmtp().getPort()));
        return Session.getDefaultInstance(properties, ixtester01.getAuthenticator());
    }

    @Test
    public void testHandleRequest() throws MessagingException, IOException, InterruptedException {
        Session session = getSession(ixtester01);
        MimeMessage message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, pioneerstest101.getUsername());
        message.setText("Hello World");
        String subject = "Test Subject 7 " + UUID.randomUUID().toString();
        message.setSubject(subject);
        message.setFrom(ixtester01.getUsername());

        MimeMessageRequest messageRequest = new MimeMessageRequest();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        messageRequest.setMessageContent(baos.toByteArray());

        EmailAccount emailAccount = new EmailAccount();
        emailAccount.setHost(ixtester01.getSmtp().getHost());
        emailAccount.setPassword(ixtester01.getPassword());
        emailAccount.setPort(ixtester01.getSmtp().getPort());
        emailAccount.setUsername(ixtester01.getUsername());
        messageRequest.setEmailAccount(emailAccount);
        Context ctx = mock(Context.class);
        LambdaLogger llog = mock(LambdaLogger.class);
        when(ctx.getLogger()).thenReturn(llog);
        doAnswer(arg -> {LOG.info(arg); return null;}).when(llog).log(anyString());

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(messageRequest));

        sender.handleRequest(messageRequest, ctx);

        /*LOG.info("start verification process");
        Duration maxWait = Duration.ofMinutes(1);
        //verify in the receiver account
        Session session2 = getSession(pioneerstest101);
        Store store = session2.getStore("imaps");
        store.connect(pioneerstest101.getImap().getHost(), pioneerstest101.getImap().getPort(), pioneerstest101.getUsername(), pioneerstest101.getPassword());
        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);
        while (maxWait.toMillis() >= 0) {
            Optional<Message> optMsg = Arrays.stream(folder.search(new SearchTerm() {
                @Override
                public boolean match(Message msg) {
                    try {
                        return msg.getSubject().equals(subject)
                                && ((InternetAddress)msg.getRecipients(Message.RecipientType.TO)[0]).getAddress().equals(pioneerstest101.getUsername())
                                && ((InternetAddress)msg.getFrom()[0]).getAddress().equals(ixtester01.getUsername());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            })).findAny();
            if (optMsg.isPresent()) {
                LOG.info(String.format("Message found with subject: %s", optMsg.get().getSubject()));
                break;
            } else {
                LOG.info(String.format("No message found, waiting for 10s, remaining wait time %ds", maxWait.getSeconds()));
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                maxWait.minus(Duration.ofMillis(10000));
            }

        }
        assertFalse("Could retrieve message from inbox, timed out.", maxWait.isNegative());*/
    }
}
