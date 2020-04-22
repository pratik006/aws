package com.prapps.aws.email;

import java.util.Objects;

public abstract class MessageRequest {
    private EmailMessage emailMessage;
    private EmailAccount emailAccount;
    private String timestamp;
    private MessageType messageType;

    public MessageRequest(){}

    public MessageRequest(MessageType messageType) {
        this.messageType = messageType;
    }

    public EmailAccount getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(EmailAccount emailAccount) {
        this.emailAccount = emailAccount;
    }

    public EmailMessage getEmailMessage() {
        return emailMessage;
    }

    public void setEmailMessage(EmailMessage emailMessage) {
        this.emailMessage = emailMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }



    @Override
    public String toString() {
        return "MessageRequest{" +
                "emailMessage=" + emailMessage +
                ", emailAccount=" + emailAccount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRequest that = (MessageRequest) o;
        return Objects.equals(emailMessage, that.emailMessage) &&
                Objects.equals(emailAccount, that.emailAccount) &&
                Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailMessage, emailAccount, timestamp);
    }
}

