package com.db.chat.core;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private Date time;
    private String text;
    private MessageType type;
    private String nick;

    public Message() {
    }

    public Message(Date time, String text, MessageType type, String nick) {
        if (time != null) {
            this.time = (Date) time.clone();
        } else {
            this.time = null;
        }
        this.text = text;
        this.type = type;
        this.nick = nick;
    }

    public Message(Date time, String text, String type, String nick) {
        this(time, text, MessageType.valueOf(type), nick);
    }

    public Message(long milisec, String text, MessageType type, String nick) {
        this(new Date(milisec), text, type, nick);
    }

    public Date getTime() {
        if (time != null) {
            return (Date) time.clone();
        } else {
            return null;
        }
    }

    public void setTime(Date time) {
        if (time != null) {
            this.time = (Date) time.clone();
        } else {
            this.time = null;
        }
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
