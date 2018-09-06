package com.db.chat;

import java.util.ArrayList;

public class Client {
    private ServerInterface server;
    private View view;

    public Client(ServerInterface server) {
        this(server, new ConsoleView());
    }

    public Client(ServerInterface server, View view) {
        this.server = server;
        this.view = view;
        this.view.setClient(this);
        server.connect(this);
        new Thread(this.view).start();
    }

    public int getHistory(){
        server.getHistory();
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

    public void quit() {
        Thread.currentThread().stop();
    }

    public static void main(String[] args) {
        ServerHelper server = new ServerHelper("127.0.0.1", 6666);
        Client client = new Client(server);
        server.setClient(client);
    }


}
