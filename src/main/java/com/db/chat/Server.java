package com.db.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements ServerInterface{
    private History history;
    private List<ClientSession> clients;

    class MessageGetter implements Runnable {
        public void handle(String textMessage) {
            Message message = deserializeMessage(textMessage);
            switch(message.getType()) {
                case MESSAGE:
                    receive(message);
                    break;
                case HISTORY:
                    getHistory();
                    break;
                case ERROR:
                    break;
            }
        }
        @Override
        public void run() {
            ExecutorService pool = Executors.newFixedThreadPool(10);
            while (!Thread.interrupted()) {
                try {
                    for (ClientSession client : clients) {
                        if (client.isNewMessageAvailable()) {
                            pool.execute(() -> {
                                try {
                                    handle(client.readMessage());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                } catch(ConcurrentModificationException e) {

                } catch (Exception e) {
                    System.out.println("unknown exception");
                    e.printStackTrace();
                }
            }
            pool.shutdownNow();
        }
    }

    public Server() throws HistoryException {
        this(new History());
    }

    public Server(History history) {
        this.history = history;
        clients = new LinkedList<>();
        new Thread(new MessageGetter()).start();
    }

    public void receive(Message message) {
        message.setTime(new Date());
        try {
            this.history.addMessage(message);
        } catch (HistoryException e) {
            e.printStackTrace();
        }
        this.send(message);
    }

    public void send(Message message) {
        for (ClientSession client : this.clients) {
            client.sendMessage(serializeMessage(message));
        }
    }

    public void getHistory() {
        try {
            for (Message message : history.getHistory()) {
                send(message);
            }
        } catch (HistoryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        //TODO here would be closing of server... maybe
    }

    public void run() {
        try (ServerSocket portListener = new ServerSocket(6666)) {
            portListener.setSoTimeout(1000);
            while (!Thread.interrupted()) {
                try {
                    Socket clientConnection = portListener.accept();
                    clients.add(new ClientSession(clientConnection));
                } catch (SocketTimeoutException e) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws HistoryException {
        new Thread(new Server()).start();
    }
}