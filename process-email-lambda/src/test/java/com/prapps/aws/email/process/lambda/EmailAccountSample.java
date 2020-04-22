package com.prapps.aws.email.process.lambda;

import static com.prapps.aws.email.process.lambda.EmailServer.*;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public enum EmailAccountSample {
    ixtester01("ixtester01@ixemailtester.onmicrosoft.com", "Pega@1234!", EXCHANGE_SMTP, null),
    pioneerstest101("pioneerstest101@gmail.com", "chinat0wn", GMAIL_SMTP, GMAIL_IMAP),
    pioneerstest102("pioneerstest102@gmail.com", "chinat0wn", GMAIL_SMTP,GMAIL_IMAP);

    private String username;
    private String password;
    private EmailServer smtp;
    private EmailServer imap;


    EmailAccountSample(String username, String password, EmailServer server, EmailServer imap) {
        this.username = username;
        this.password = password;
        this.smtp = server;
        this.imap = imap;
    }

    public String getUsername() {
        return username;
    }

    public EmailServer getSmtp() {
        return smtp;
    }

    public EmailServer getImap() {
        return imap;
    }

    public String getPassword() {
        return password;
    }

    public Authenticator getAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };
    }
}
