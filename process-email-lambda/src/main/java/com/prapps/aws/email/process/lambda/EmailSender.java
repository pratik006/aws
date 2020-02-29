package com.prapps.aws.email.process.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.prapps.aws.email.EmailAccount;
import com.prapps.aws.email.EmailMessage;
import com.prapps.aws.email.MessageRequest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Properties;

public class EmailSender {

    public String handleRequest(MessageRequest messageRequest, Context context) {
        context.getLogger().log("messageRequest: " + messageRequest);

        Properties properties = new Properties();
        final EmailAccount emailAccount = messageRequest.getEmailAccount();
        properties.setProperty("mail.smtp.host", emailAccount.getHost());
        properties.setProperty("mail.smtp.port", String.valueOf(emailAccount.getPort()));
        //properties.put("mail.smtp.socketFactory.port", emailAccount.getPort()); //SSL Port
        //properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        properties.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", emailAccount.getHost());

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getUsername(), emailAccount.getPassword());
            }
        };
        Session session = Session.getInstance(properties, auth);
        try {
            MimeMessage message = compose(session, messageRequest.getEmailMessage());
            Transport.send(message);
            context.getLogger().log("Mail sent successfully");
            return "Message sent successfully - " + messageRequest;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Message sending failed" + messageRequest;
        }
    }

    public MimeMessage compose(Session session, EmailMessage emailMessage) throws UnsupportedEncodingException, MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress(emailMessage.getFrom(), "pioneerstest102"));
        msg.setReplyTo(InternetAddress.parse(emailMessage.getFrom(), false));
        msg.setSubject(emailMessage.getSubject(), "UTF-8");
        msg.setText(emailMessage.getTextBody(), "UTF-8");
        msg.setSentDate(Calendar.getInstance().getTime());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailMessage.getRecipients(), false));
        return msg;
    }
}
