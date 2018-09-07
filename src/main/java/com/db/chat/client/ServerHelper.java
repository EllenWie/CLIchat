package com.db.chat.client;

import com.db.chat.core.Chat;
import com.db.chat.core.Message;
import com.db.chat.core.MessageType;

import java.io.IOException;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ServerHelper class is needed
 * to make client more easy to understand.
 * All network is encapsulated in this class.
 * Its objects create connection to server.
 * Has function to read from socket by lines
 * and to write to socket by lines.
 */
public class ServerHelper implements Chat {
    transient private Client client;
    transient private Socket socket;
    transient private PrintWriter out;
    transient private BufferedReader in;

    public ServerHelper(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(
                    new OutputStreamWriter(
                            new BufferedOutputStream(
                                    socket.getOutputStream())));
            in = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    socket.getInputStream())));
        } catch (UnknownHostException e) {
            System.out.println("Error: Can't connect to server" + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return if socket was not set up
     * @return
     */
    public boolean isConnected() {
        return socket != null;
    }

    /**
     * closes connection to server
     */
    @Override
    public void close() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this function immitates server's receive message
     * to make client's methods work as if it is real server.
     * after getting message it sends it throug socket
     * @param message
     */
    @Override
    public void receive(Message message) {
        if (out != null) {
            out.println(serializeMessage(message));
            out.flush();
        }
    }

    /**
     * this methods invokes when message has been received from socket
     * and calls clients receive method.
     * @param message
     */
    @Override
    public void send(Message message) {
        client.receive(message);
    }

    /**
     * Main cycle of serverHelper program.
     * it listens incoming messages from socket
     * and executes tasks to handle new message
     * in its fixed thread pool.
     */
    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        while(!Thread.interrupted()) {
            try {
                String textMessage = in.readLine();
                pool.execute(() ->
                    send(deserializeMessage(textMessage))
                );
            } catch (IOException e) {
                client.receive(new Message(null, "Server is down", MessageType.ERROR, client.getNick()));
                Thread.currentThread().interrupt();
            }
        }
        pool.shutdownNow();
        close();
    }

    /**
     * assigns client to serverHelper object
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }
}
