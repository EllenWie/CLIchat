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
    private volatile List<ClientSession> clients;

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
            System.out.println("handler finished");
        }
        @Override
        public void run() {
            ExecutorService pool = Executors.newFixedThreadPool(10);
            while (!Thread.interrupted()) {
                try {
                    int i = 0;
                    for (ClientSession client : clients) {
                        System.out.println(i++);
                        //TODO: doublechecking!!!!!!!
                        //if (client.isNewMessageAvailable()) {
                            if (client.lock.tryLock()) {
                                if (client.isNewMessageAvailable()) {
                                    pool.execute(() -> {
                                        try {
                                            handle(client.readMessage());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println("thread finished");
                                    });
                                    System.out.println("pool execute finished");
                                } else {
                                    System.out.println("synched but not available");
                                }
                                client.lock.unlock();
                            }
                        //}
                    }
                } catch(ConcurrentModificationException e) {
                    System.out.println("beda");
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
        clients = new LinkedList<>();//Collections.synchronizedList(new LinkedList<>());//
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
                    synchronized (clientConnection) {
                        clients.add(new ClientSession(clientConnection));
                    }
                    System.out.println("Now clients length is "+ clients.size());
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