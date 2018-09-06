package com.db.chat;

public class Client {
    private Server server;
    private View view;

    public Client(Server server) {
        this(server, new ConsoleView());
    }

    public Client(Server server, View view) {
        this.server = server;
        this.view = view;
        this.view.setClient(this);
        server.connect(this);
        new Thread(this.view).start();
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

    public void quit() {
        Thread.currentThread().stop();
    }
}
