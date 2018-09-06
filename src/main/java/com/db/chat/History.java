package com.db.chat;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class History {
    private List<Message> history;
    private ExecutorService pool = newCachedThreadPool();
    private final String historyFileName = "history.txt";
    private ObjectOutputStream historyWriter;
    private ObjectInputStream historyReader;

    public History() throws HistoryException {
        this.history = new LinkedList<>();
        initIOStreams();
    }

    public History(History history) throws HistoryException {
        this.history = history.getHistory();
        initIOStreams();
    }

    public List<Message> getHistory() throws HistoryException {
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
        try {
            if (result.get(1000, TimeUnit.MILLISECONDS)) {
                return;
            }
        } catch (TimeoutException | InterruptedException | ExecutionException e) {
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
