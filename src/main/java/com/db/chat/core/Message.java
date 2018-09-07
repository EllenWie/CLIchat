package com.db.chat.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Data object to store and transmit messages.
 * It has four fields:
 * Date time - Date object to store date and time
 * String text - text of transmitted message
 * MessageType type - type of message
 * String nick - author's nick for the message
 */
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

    /**
     * you can create message object with passing time in milliseconds
     * @param milisec
     * @param text
     * @param type
     * @param nick
     */
    public Message(long milisec, String text, MessageType type, String nick) {
        this(new Date(milisec), text, type, nick);
    }

    /**
     * getter for time
     * @return
     */
    public Date getTime() {
        if (time != null) {
            return (Date) time.clone();
        } else {
            return null;
        }
    }

    /**
     * setter for time
     * @param time
     */
    public void setTime(Date time) {
        if (time != null) {
            this.time = (Date) time.clone();
        } else {
            this.time = null;
        }
    }

    /**
     * getter for message text
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * setter for message text
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * getter of message type
     * @return
     */
    public MessageType getType() {
        return type;
    }

    /**
     * setter of message type
     * @param type
     */
    public void setType(MessageType type) {
        this.type = type;
    }

    /**
     * converting object to string
     * @return
     */
    @Override
    public String toString() {
        return time.toString() + ": " + text;
    }

    /**
     * getter of author's nick
     * @return
     */
    public String getNick() {
        return nick;
    }

    /**
     * setter of author's nick
     * @param nick
     */
    public void setNick(String nick) {
        this.nick = nick;
    }
}
