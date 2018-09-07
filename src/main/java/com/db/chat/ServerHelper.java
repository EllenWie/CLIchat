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
            System.out.println("Can't connect to server");
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
            System.out.println("Have no connection to server");
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
                System.out.println("server is down");
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
