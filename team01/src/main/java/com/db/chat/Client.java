package com.db.chat;

import java.util.ArrayList;

public class Client {
    private Server server;

    public ArrayList<Message> getHistory(){
        return server.getHistory();
    }
}
