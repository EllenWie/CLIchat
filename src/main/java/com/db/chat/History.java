package com.db.chat;

import java.util.ArrayList;

public class History {
    private ArrayList<Message> history;

    public History() {
        this.history = new ArrayList<>();
    }

    public History(History history) {
        this.history = history.getHistory();
    }

    public ArrayList<Message> getHistory() {
        return history;
    }

    public void addMessage(Message message) {
        history.add(message);
    }
}
