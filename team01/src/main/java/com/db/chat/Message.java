package com.db.chat;

public class Message {
    private String time;
    private String text;

    public Message(String time, String text) {
        this.time = time;
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }
}
