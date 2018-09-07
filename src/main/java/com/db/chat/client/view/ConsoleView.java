package com.db.chat.client.view;

import com.db.chat.client.Client;
import com.db.chat.core.Message;
import com.db.chat.core.MessageType;

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
                if (nickIsSet()) {
                    String text = arguments.length > 1 ? arguments[1].trim() : "";
                    if (text.length() > 0) {
                        client.send(new Message(null, text, MessageType.MESSAGE, client.getNick()));
                    } else {
                        display(new Message(null, "Empty message. Was not sent to server.", MessageType.ERROR, client.getNick()));
                    }
                }
                break;
            case "/hist":
                if (nickIsSet()) {
                    client.send(new Message(null, null, MessageType.HISTORY, client.getNick()));
                }
                break;
            case "/chid":
                String newNick = arguments.length > 1 ? arguments[1].trim() : "";
                if (newNick.length() > 0) {
                    client.setNick(newNick);
                    System.out.println("Your nick was set" + System.lineSeparator());
                } else {
                    display(new Message(null, "Nick must not be empty", MessageType.ERROR, client.getNick()));
                }
                break;
            default:
                display(new Message(null, "Wrong command", MessageType.ERROR, client.getNick()));
        }
    }

    private boolean nickIsSet() {
        if (client.getNick() == null) {
            display(new Message(null, "You should set nick before starting using the app", MessageType.ERROR, client.getNick()));
            return false;
        }
        return true;
    }

    @Override
    public void display(Message message) {
        switch (message.getType()) {
            case MESSAGE:
                System.out.println(message.getTime().toString() + " [" + message.getNick() + "]" + System.lineSeparator()
                        + message.getText() + System.lineSeparator());
                break;
            case ERROR:
                System.out.println("Error: " + message.getText() + System.lineSeparator());
                break;
        }
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
