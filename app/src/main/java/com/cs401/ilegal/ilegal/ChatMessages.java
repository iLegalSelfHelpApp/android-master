package com.cs401.ilegal.ilegal;

/**
 * Created by seanyuan on 11/28/17.
 */

import java.util.Date;

public class ChatMessages {
    private String messageText;
    private String messageUser;
    private String messageUserId;
    private long messageTime;

    public ChatMessages(String messageText, String messageUser, String messageUserId) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        messageTime = new Date().getTime();
        this.messageUserId = messageUserId;
    }

    public ChatMessages(){

    }

    public String getMessageUserId() {
        return messageUserId;
    }

    public void setMessageUserId(String messageUserId) {
        this.messageUserId = messageUserId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}