package com.db.chat;

import java.util.ArrayList;

public interface ServerInterface {
    void receive(Message message);

    void send(Message message);

    public void getHistory();
}
