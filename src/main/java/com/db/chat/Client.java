package com.db.chat;

public class Client {
    private Server server;
    private View view;

    public Client(Server server, View view) {
        this.server = server;
        this.view = view;
        server.connect(this);
    }

    public int getHistory(){
        view.displayHistory(server.getHistory());
        return 0;
    }

    public int send(Message message) {
        server.receive(message);
        return 0;
    }

    public int receive(Message message) {
        view.display(message);
        return 0;
    }

}
