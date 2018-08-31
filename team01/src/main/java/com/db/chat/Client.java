package com.db.chat;

public class Client {
    private Server server;

    public Message[] getHistory(){
        return server.getHistory();
    }
}
