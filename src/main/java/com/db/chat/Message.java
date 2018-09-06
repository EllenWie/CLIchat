package com.db.chat;

import java.util.Date;

public class Message {
    private Date time;
    private String text;
    private MessageType type;

    public Message(Date time, String text, MessageType type) {
        this.time = time;
        this.text = text;
        this.type = type;
    }

    public Message(long milisec, String text, MessageType type) {
        this(new Date(milisec), text, type);
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public MessageType getType() {
        return type;
    }

    @Override
    public String toString() {
        return time.toString() + ": " + text;
    }
}
