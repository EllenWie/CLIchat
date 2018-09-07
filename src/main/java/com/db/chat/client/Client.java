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
 * To start application run main function.
 * You should pass correct server address to Client constructor.
 * After creation run start() method
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
     * returns user's nick
     * @return nick
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
     * sends message object to server.
     * Calls serverHelper's method to do it.
     * serverHelper takes responsibility for network
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
     * use this function right after creation client object
     */
    public void start() {
        if (((ServerHelper)this.server).isConnected()) {
            socketThread.start();
        }
        viewThread.start();
    }

    /**
     * shut down of client application
     * interrupts its threads to finish them correctly
     * use this function when correct shutdown needed
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
     * Client object creates and starts
     * Pass correct arguments to constructor
     * host - server's host
     * port - port of application on server's host
     * @param args
     */
    public static void main(String... args) {
        Client client = new Client("127.0.0.1", 6666);
        client.start();
    }


}
