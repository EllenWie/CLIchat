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

/**
 * ConsoleView is an object that provides
 * access to console.
 * Its run function goes in the loop
 * listening to console input.
 * It stops after /quit command received.
 * It provides method to print Messages
 * to console with formating.
 * It has backward reference to Client
 * object to communicate with it.
 */
public class ConsoleView implements View {
    transient private Client client;

    /**
     * Assigns this object to the client object
     * that uses it.
     * It is necessary to get access to client's method
     * that sends messages through socket.
     * @param client
     */
    @Override
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * This method is getting string message
     * and handle it.
     * After parsing message this function
     * invokes appropriate client's methods
     * @param currentMessage
     */
    @Override
    public void send(String currentMessage) {
        String[] arguments = currentMessage.split(" ", 2);
        String type = arguments[0];
        switch (type) {
            case "/snd":
                handleSendMessage(arguments);
                break;
            case "/hist":
                handleHistCommand();
                break;
            case "/chid":
                handleChidCommand(arguments);
                break;
            default:
                display(new Message(null, "Wrong command", MessageType.ERROR, client.getNick()));
        }
    }

    private void handleChidCommand(String[] arguments) {
        String newNick = getCommandData(arguments);
        if (newNick.length() > 0) {
            client.setNick(newNick);
            System.out.println("Your nick was set" + System.lineSeparator());
        } else {
            display(new Message(null, "Nick must not be empty", MessageType.ERROR, client.getNick()));
        }
    }

    private String getCommandData(String[] arguments) {
        if (arguments.length > 1) {
            return arguments[1].trim();
        } else {
            return "";
        }
    }

    private void handleHistCommand() {
        if (nickIsSet()) {
            client.send(new Message(null, null, MessageType.HISTORY, client.getNick()));
        }
    }

    private void handleSendMessage(String[] arguments) {
        if (nickIsSet()) {
            String text = getCommandData(arguments);
            if (text.length() > 0) {
                client.send(new Message(null, text, MessageType.MESSAGE, client.getNick()));
            } else {
                display(new Message(null, "Empty message. Was not sent to server.", MessageType.ERROR, client.getNick()));
            }
        }
    }

    private boolean nickIsSet() {
        if (client.getNick() == null) {
            display(new Message(null, "You should set nick before starting using the app", MessageType.ERROR, client.getNick()));
            return false;
        }
        return true;
    }

    /**
     * Displaying function.
     * It displays message in the console
     * depending on message's type.
     * It can display user messages in format:
     * Date [author] message
     * and error messages in format:
     * Error: message
     * @param message
     */
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
            default:
                System.out.println("Error: wrong message type");
        }
    }

    /**
     * main cycle of ConsoleView object.
     * it runs in the loop listening
     * to user's input until getting
     * /quit command.
     * It has fixed thread pool to handle
     * every message it has read from console
     * in different thread.
     * Pool is using because of the
     * short time of life of handlers
     * and cost of creating and distructing
     * new threads.
     */
    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(System.in)))) {
            while (!Thread.interrupted()) {
                String currentMessage = bufferedReader.readLine();
                handleCommand(pool, currentMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.shutdownNow();
    }

    private void handleCommand(ExecutorService pool, String currentMessage) {
        if ("/quit".equals(currentMessage.split(" ", 2)[0])) {
            client.quit();
        } else {
            pool.execute(() -> {
                send(currentMessage);
            });
        }
    }
}
