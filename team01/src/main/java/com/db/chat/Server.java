package com.db.chat;

public class Server {
    private History history;

    public Server(History history) {
        this.history = history;
    }

    public void receive(Message message) {

    }

    public void send(Message message) {

    }

    public Message[] getHistory() {

        return history.getHistory();
    }
}
