package com.db.chat;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class History {
    private ArrayList<Message> history;
    private ExecutorService pool = newCachedThreadPool();
    private final String historyFileName = "history.txt";
    private ObjectOutputStream historyWriter;
    private ObjectInputStream historyReader;

    public History() throws HistoryException {
        this.history = new ArrayList<>();
        initIOStreams();
    }

    public History(History history) throws HistoryException {
        this.history = history.getHistory();
        initIOStreams();
    }

    public ArrayList<Message> getHistory() throws HistoryException {
        Message currentMessage;
        try {
            while ((currentMessage = (Message) historyReader.readObject()) != null) {
                history.add(currentMessage);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new HistoryException("Couldn't get history", e);
        }
        return history;
    }

    public void addMessage(Message message) throws HistoryException {
        Future<Boolean> result = pool.submit(() -> {
            try {
                historyWriter.writeObject(message);
            } catch (IOException e) {
                return false;
            }
            return true;
        });
        while (!result.isDone()) {
            if (result.isCancelled()) {
                throw new HistoryException("Could't add message");
            }
        }
        try {
            if (result.get()) {
                return;
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new HistoryException("Could't add message", e);
        }
    }

    private void initIOStreams() throws HistoryException {
        try {
            historyWriter =
                    new ObjectOutputStream(
                            new FileOutputStream(
                                    new File(".", historyFileName),
                                    true
                            )
                    );

            historyReader =
                    new ObjectInputStream(
                            new FileInputStream(new File(".", historyFileName))
                    );
        } catch (IOException e) {
            throw new HistoryException("Couldn't init history", e);
        }
    }
}
