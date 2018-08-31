package com.db.chat;

import java.util.ArrayList;

public interface View {
    void send();
    void display(Message message);

    void displayHistory(ArrayList<Message> history);
}
