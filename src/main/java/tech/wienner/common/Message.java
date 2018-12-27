package tech.wienner.common;

import java.util.Date;

public class Message {
    private String text;
    private Date date;
    private Sender sender;

    public Message(String text, Date date, Sender sender) {
        this.text = text;
        this.date = date;
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public Sender getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(sender.toString() + "\t")
                .append(date.toLocaleString() + System.lineSeparator())
                .append(text + System.lineSeparator())
                .toString();
    }
}
