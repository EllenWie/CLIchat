package com.db.chat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsoleView implements View {
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void send(String currentMessage) {
        String type = currentMessage.substring(0, 5);
        switch (type) {
            case "/snd ":
                String text = currentMessage.substring(5);
                client.send(new Message(null, text, Message.MessageType.MESSAGE));
                break;
            case "/hist":
                client.send(new Message(null, null, Message.MessageType.HISTORY));
                break;
            case "/quit":
                client.quit();
                break;
            default:
                System.out.println("wrong command");
        }
    }

    @Override
    public void display(Message message) {
        System.out.println(message.getTime().toString() + System.lineSeparator()
                + message.getText() + System.lineSeparator());
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(System.in)))) {
            while (!Thread.interrupted()) {
                String currentMessage = bufferedReader.readLine();
                pool.execute(() -> {
                    send(currentMessage);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.shutdownNow();
        System.out.println("consoleview finished");
    }
}
