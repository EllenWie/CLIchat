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
        String[] arguments = currentMessage.split(" ", 2);
        String type = arguments[0];
        switch (type) {
            case "/snd":
                if (arguments.length > 1) {
                    String text = arguments[1].trim();
                    if (text.length() > 0) {
                        client.send(new Message(null, text, MessageType.MESSAGE));
                    } else {
                        System.out.println("Empty message. Was not sent to server.");
                    }
                } else {
                    System.out.println("Empty message. Was not sent to server.");
                }
                break;
            case "/hist":
                client.send(new Message(null, null, MessageType.HISTORY));
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
                if ("/quit".equals(currentMessage.split(" ", 2)[0])) {
                    client.quit();
                } else {
                    pool.execute(() -> {
                        send(currentMessage);
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.shutdownNow();
    }
}
