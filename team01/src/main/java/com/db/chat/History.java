package com.db.chat;

import java.util.ArrayList;

public class History {
    private ArrayList<Message> history;

    public History() {
        this.history = new ArrayList<>();
    }

    public History(ArrayList<Message> history) {
        this.history = history;
    }

    public ArrayList<Message> getHistory() {
        return history;
    }
}
