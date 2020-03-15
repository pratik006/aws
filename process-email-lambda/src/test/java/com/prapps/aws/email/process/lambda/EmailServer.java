package com.prapps.aws.email.process.lambda;

public enum EmailServer {
    GMAIL_SMTP("smtp.gmail.com", 587),
    GMAIL_IMAP("imap.gmail.com", 993),
    EXCHANGE_SMTP("smtp.office365.com", 587);

    private String host;
    private int port;


    EmailServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
