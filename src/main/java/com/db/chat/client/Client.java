package com.db.chat.client;

import com.db.chat.client.view.ConsoleView;
import com.db.chat.client.view.View;
import com.db.chat.core.Chat;
import com.db.chat.core.Message;
import com.db.chat.core.MessageType;

import java.io.IOException;

/**
 * Client class is main client application.
 * It can send messages to server and receive from it.
 */
public class Client {
    transient private Chat server;
    transient private View view;
    transient private Thread viewThread, socketThread;
    private static final int inputConstraint = 150;
    private String nick;

    public Client(String host, int port) {
        this(new ServerHelper(host, port));
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

    /**
     * @return user's nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * sets user's nick
     * @param nick
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * sends message object to server (through helper)
     * @param message
     * @return
     */
    public int send(Message message) {
        if (message.getType() == MessageType.MESSAGE && message.getText().length() > inputConstraint) {
            view.display(new Message(null, "Too long message", MessageType.ERROR, nick));
        } else {
            server.receive(message);
        }
        return 0;
    }

    /**
     * this method invokes whenever new message from server had been received
     * @param message
     * @return
     */
    public int receive(Message message) {
        view.display(message);
        return 0;
    }

    /**
     * start this method after creating Client object to run all needed threads
     */
    public void start() {
        if (((ServerHelper)this.server).isConnected()) {
            socketThread.start();
        }
        viewThread.start();
    }

    /**
     * shut down of client application
     */
    public void quit() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewThread.interrupt();
        socketThread.interrupt();
    }

    /**
     * Enter point to client application
     * @param args
     */
    public static void main(String... args) {
        Client client = new Client("127.0.0.1", 6666);
        client.start();
    }


}
