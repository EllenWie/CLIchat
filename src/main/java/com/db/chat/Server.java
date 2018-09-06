package com.db.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements ServerInterface{
    private HistoryController historyController;
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
                    int i = 0;
                    for (ClientSession client : clients) {
                        System.out.println(i++);
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

    public Server() throws HistoryControllerException {
        this(new HistoryController());
    }

    public Server(HistoryController historyController) {
        this.historyController = historyController;
        clients = Collections.synchronizedList(new LinkedList<>());
        new Thread(new MessageGetter()).start();
    }

    public void receive(Message message) {
        System.out.println("received message: "+ message.getText());
        message.setTime(new Date());
        try {
            this.historyController.addMessage(message);
        } catch (HistoryControllerException e) {
            e.printStackTrace();
        }
        this.send(message);
        System.out.println("sended to all (from receive)"+ message.getText());
    }

    public void send(Message message) {
        for (ClientSession client : this.clients) {
            client.sendMessage(serializeMessage(message));
        }
        System.out.println("sended to all "+ message.getText());
    }

    public void getHistory() {
        try {
            for (Message message : historyController.getHistory()) {
                send(message);
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