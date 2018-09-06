package com.db.chat;

import java.util.ArrayList;

public interface View extends Runnable{
    void send(String message);
    void display(Message message);
    void setClient(Client client);
}
