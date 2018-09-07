package com.db.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Chat {
    private HistoryController historyController;
    private volatile List<ClientSession> clients;

    class MessageGetter implements Runnable {
        public void handle(ClientSession client, String textMessage) {
            Message message = deserializeMessage(textMessage);
            switch(message.getType()) {
                case MESSAGE:
                    receive(message);
                    break;
                case HISTORY:
                    getHistory(client);
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
                        if (client.lock.tryLock()) {
                            if (client.isNewMessageAvailable()) {
                                pool.execute(() -> {
                                    try {
                                        handle(client, client.readMessage());
                                    } catch (NullPointerException e) {
                                        clients.remove(client);
                                    } catch (IOException e) {
                                        System.out.println("io exception");
                                        e.printStackTrace();
                                    }
                                });
                            }
                            client.lock.unlock();
                        }
                    }
                } catch(ConcurrentModificationException e) {
                } catch (NullPointerException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            pool.shutdownNow();
        }
    }

    public Server() throws HistoryControllerException {
        this(new HistoryController());
    }

    public Server(HistoryController historyController) {
        this.historyController = historyController;
        clients = new LinkedList<>();//Collections.synchronizedList(new LinkedList<>());//
        new Thread(new MessageGetter()).start();
    }

    public void receive(Message message) {
        message.setTime(new Date());
        try {
            this.historyController.addMessage(message);
        } catch (HistoryControllerException e) {
            e.printStackTrace();
        }
        this.send(message);
    }

    public void send(Message message) {
        for (ClientSession client : this.clients) {
            client.sendMessage(serializeMessage(message));
        }
    }

    public void getHistory(ClientSession client) {
        try {
            for (Message message : historyController.getHistory()) {
                client.sendMessage(serializeMessage(message));
            }
        } catch (HistoryControllerException e) {
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

    public static void main(String[] args) throws HistoryControllerException {
        new Thread(new Server()).start();
    }
}