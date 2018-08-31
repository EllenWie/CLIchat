package com.db.chat;

import java.util.ArrayList;

public class Client {
    private Server server;

    public Client(Server server) {
        this.server = server;
        server.connect(this);
    }

    public ArrayList<Message> getHistory(){
        return server.getHistory();
    }

    public int send(Message message) {
        server.receive(message);
        return 0;
    }

    public int receive(Message message) {
        return 0;
    }

}
