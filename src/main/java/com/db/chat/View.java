package com.db.chat;

import java.util.ArrayList;

public interface View extends Runnable{
    void send();
    void display(Message message);
    void setClient(Client client);

    void displayHistory(ArrayList<Message> history);
}
