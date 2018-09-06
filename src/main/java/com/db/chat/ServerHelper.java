package com.db.chat;

import java.io.*;
import java.net.Socket;

public class ServerHelper implements ServerInterface, Runnable, Closeable {
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
            System.out.println("server helper constructor error");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("close exception");
            e.printStackTrace();
        }
    }

    @Override
    public void receive(Message message) {
        out.println(serializeMessage(message));
        out.flush();
    }

    @Override
    public void send(Message message) {
        client.receive(message);
    }

    @Override
    public void getHistory() {
        out.println("/hist");
        out.flush();
    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                String textMessage = in.readLine();
                send(deserializeMessage(textMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
