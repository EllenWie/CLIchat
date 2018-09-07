package com.db.chat;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private Date time;
    private String text;
    private MessageType type;

    public Message() {
    }

    public Message(Date time, String text, MessageType type) {
        this.time = (Date) time.clone();
        this.text = text;
        this.type = type;
    }

    public Message(Date time, String text, String type) {
        this(time, text, MessageType.valueOf(type));
    }

    public Message(String text, String type) {
        this(null, text, MessageType.valueOf(type));
    }

    public Message(long milisec, String text, MessageType type) {
        this(new Date(milisec), text, type);
    }

    public Date getTime() {
        return (Date) time.clone();
    }

    public void setTime(Date time) {
        this.time = (Date) time.clone();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return time.toString() + ": " + text;
    }
}
