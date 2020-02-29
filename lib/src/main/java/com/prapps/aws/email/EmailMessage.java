package com.prapps.aws.email;

import java.util.Objects;

public class EmailMessage {
    private String subject;
    private String textBody;
    private String recipients;
    private String from;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "EmailMessage{" +
                "subject='" + subject + '\'' +
                ", textBody='" + textBody + '\'' +
                ", recipients='" + recipients + '\'' +
                ", from='" + from + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailMessage that = (EmailMessage) o;
        return Objects.equals(subject, that.subject) &&
                Objects.equals(textBody, that.textBody) &&
                Objects.equals(recipients, that.recipients) &&
                Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, textBody, recipients, from);
    }
}
