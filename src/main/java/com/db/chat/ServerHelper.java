package com.db.chat;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHelper implements ServerInterface, Runnable, Closeable {
    private Client client;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectMapper mapper;

    private String serializeMessage(Message message) {
        try {
            return mapper.writeValueAsString(message);
        } catch (IOException e) {
            System.out.println("serialize error");
            e.printStackTrace();
            return null;
        }
    }

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
        mapper = new ObjectMapper();
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
    }

    @Override
    public void send(Message message) {
        client.receive(message);
    }

    @Override
    public void getHistory() {
        out.println("/hist");
    }

    @Override
    public void connect(Client client) {

    }

    @Override
    public void run() {
        //while(true) receive message
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
