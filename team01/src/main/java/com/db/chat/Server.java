package com.db.chat;

import java.util.ArrayList;

public class Server {
    private History history;

    public Server(History history) {
        this.history = history;
    }

    public void receive(Message message) {

    }

    public void send(Message message) {

    }

    public ArrayList<Message> getHistory() {

        return history.getHistory();
    }
}
