package com.db.chat;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHelper implements Chat {
    private Client client;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

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
        } catch (Exception e) {
            //client.receive(new Message(null, "Can't connect to server", MessageType.ERROR));
            System.out.println("Error: Can't connect to server" + System.lineSeparator());
        }
    }

    public boolean isConnected() {
        return (socket != null);
    }

    public void close() {
        try {
            socket.close();
            in.close();
            out.close();
            socket = null;
            in = null;
            out = null;
        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message message) {
        try {
            out.println(serializeMessage(message));
            out.flush();
        } catch (NullPointerException e) {
            client.receive(new Message(null, "Have no connection to server", MessageType.ERROR));
        }
    }

    @Override
    public void send(Message message) {
        client.receive(message);
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        while(!Thread.interrupted()) {
            try {
                String textMessage = in.readLine();
                pool.execute(() -> {
                    send(deserializeMessage(textMessage));
                });
            } catch (IOException e) {
                client.receive(new Message(null, "Server is down", MessageType.ERROR));
                Thread.currentThread().interrupt();
            }
        }
        pool.shutdownNow();
        close();
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
