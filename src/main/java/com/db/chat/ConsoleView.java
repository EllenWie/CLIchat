package com.db.chat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

public class ConsoleView implements View {
    private String currentMessage;
    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void send() {
        String type = currentMessage.substring(0, 5);
        switch (type) {
            case "/snd ":
                String text = currentMessage.substring(5);
                Message message = new Message(null, text);
                client.send(message);
                break;
            case "/hist":
                client.getHistory();
                break;
            case "/quit":
                client.quit();
                Thread.currentThread().stop();
                break;
            default:
                System.out.println("wrong command");
        }
    }

    @Override
    public void display(Message message) {
        System.out.println(message.getTime().toString() + System.lineSeparator() + message.getText());
    }

    @Override
    public void displayHistory(ArrayList<Message> history) {
        for (Message message : history) {
            display(message);
        }
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(System.in)))) {
            while (!Thread.interrupted()) {
                currentMessage = bufferedReader.readLine();
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //new ConsoleView().run();
        /*new Thread(() -> {
            try (BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(System.in)))) {
                while (!Thread.interrupted()) {
                    System.out.println(">>>>>"+ bufferedReader.readLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            while(!Thread.interrupted()) {
                System.out.println("1111");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

*/
    }
}
