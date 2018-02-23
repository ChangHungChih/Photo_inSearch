package idv.sean.photo_insearch.model;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    private String type;           //type: open, close, chat
    private String sender;
    private String receiver;
    private String content;
    private String messageType;    //messageType: text, image

    public ChatMessage() {
    }

    public ChatMessage(String type, String sender, String receiver, String content, String messageType) {
        super();
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.messageType = messageType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
