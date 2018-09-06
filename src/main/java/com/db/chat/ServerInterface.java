package com.db.chat;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.Closeable;
import java.io.IOException;

public interface ServerInterface extends Closeable {
    default String serializeMessage(Message message) {
        try {
            return new ObjectMapper().writeValueAsString(message);
        } catch (IOException e) {
            System.out.println("serialize error");
            e.printStackTrace();
            return null;
        }
    }

    default Message deserializeMessage(String textMessage) {
        try {
            return new ObjectMapper().readValue(textMessage, Message.class);
        } catch (IOException e) {
            System.out.println("deserialize error");
            e.printStackTrace();
            return null;
        }
    }

    void receive(Message message);

    void send(Message message);
}
