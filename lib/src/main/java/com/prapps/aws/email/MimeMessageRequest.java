package com.prapps.aws.email;

public class MimeMessageRequest extends MessageRequest {
    private byte[] messageContent;

    public MimeMessageRequest() {
        super(MessageType.MIME);
    }

    public byte[] getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(byte[] messageContent) {
        this.messageContent = messageContent;
    }
}
