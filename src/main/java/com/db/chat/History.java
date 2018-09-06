package com.db.chat;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class History {
    private ArrayList<Message> history;// = new ArrayList<>();
    private ExecutorService pool = newCachedThreadPool();
    private final String historyFileName = "history.txt";
    private ObjectOutputStream historyWriter =
            new ObjectOutputStream(
                    new FileOutputStream(
                            new File(".", historyFileName),
                            true
                    )
            );
    private ObjectInputStream historyReader =
            new ObjectInputStream(
                    new FileInputStream(new File(".", historyFileName))
            );


    public History() throws IOException {
        this.history = new ArrayList<>();
    }

    public History(History history) throws HistoryException, IOException {
        this.history = history.getHistory();
    }

    public ArrayList<Message> getHistory() throws HistoryException {
        Message currentMessage;
        try {
            while ((currentMessage = (Message) historyReader.readObject()) != null) {
                history.add(currentMessage);
            }
        } catch (ClassNotFoundException | IOException e) {
            HistoryException currentHistoryException = new HistoryException("Couldn't get history", e);
            throw currentHistoryException;
        }
        return history;
    }

    public void addMessage(Message message) throws HistoryException {
        pool.execute(() -> {
            try {
                historyWriter.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
