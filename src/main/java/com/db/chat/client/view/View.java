package com.db.chat.client.view;

import com.db.chat.client.Client;
import com.db.chat.core.Message;

public interface View extends Runnable{
    void send(String message);
    void display(Message message);
    void setClient(Client client);
}
