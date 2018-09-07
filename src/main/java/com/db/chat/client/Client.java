package com.db.chat.client;

import com.db.chat.client.view.ConsoleView;
import com.db.chat.client.view.View;
import com.db.chat.core.Chat;
import com.db.chat.core.Message;
import com.db.chat.core.MessageType;

import java.io.IOException;

public class Client {
    private Chat server;
    private View view;
    private Thread viewThread, socketThread;
    private static final int inputConstraint = 150;

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
        if (((ServerHelper)this.server).isConnected()) {
            ((ServerHelper) this.server).setClient(this);
            socketThread = new Thread((ServerHelper) this.server);
        }
    }

    public int send(Message message) {
        if (message.getType() == MessageType.MESSAGE && message.getText().length() > inputConstraint) {
            view.display(new Message(null, "Too long message", MessageType.ERROR));
        } else {
            server.receive(message);
        }
        return 0;
    }

    public int receive(Message message) {
        view.display(message);
        return 0;
    }

    public void start() {
        if (((ServerHelper)this.server).isConnected()) {
            socketThread.start();
        }
        viewThread.start();
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
        client.start();
    }


}
