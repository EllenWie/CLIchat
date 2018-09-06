package com.db.chat;

import java.util.Date;

public class Message {
    private Date time;

    private String text;
    public Message(Date time, String text) {
        this.time = time;
        this.text = text;
    }

    public Message(long milisec, String text) {
        this.time = new Date(milisec);
        this.text = text;
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
}
