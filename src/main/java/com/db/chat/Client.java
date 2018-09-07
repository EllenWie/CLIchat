package com.db.chat;

import java.io.IOException;

public class Client {
    private Chat server;
    private View view;
    private Thread viewThread, socketThread;

    public Client() {
        this(new ServerHelper("127.0.0.1", 6666));
    }

    public Client(Chat server) {
        this(server, new ConsoleView());
    }

    public Client(Chat server, View view) {
        this.server = server;
        this.view = view;
        this.view.setClient(this);
        viewThread = new Thread(this.view);
        viewThread.start();
        if (((ServerHelper)this.server).isConnected()) {
            ((ServerHelper) this.server).setClient(this);
            socketThread = new Thread((ServerHelper) this.server);
            socketThread.start();
        }
    }

    public int send(Message message) {
        if (message.getType() == MessageType.MESSAGE && message.getText().length() > 150) {
            System.out.println("Error: Too long message");
        } else {
            server.receive(message);
        }
        return 0;
    }

    public int receive(Message message) {
        view.display(message);
        return 0;
    }

    public void quit() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewThread.interrupt();
        socketThread.interrupt();
    }

    public static void main(String[] args) {
        Client client = new Client();
    }


}
