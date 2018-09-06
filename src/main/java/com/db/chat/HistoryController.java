package com.db.chat;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.currentThread;


public class HistoryController {
    private List<Message> history;
    private BlockingQueue<Callable<Boolean>> taskQueue = new LinkedBlockingQueue<>();
    private final String historyFileName = "history.txt";
    private ObjectOutputStream historyWriter;
    private ExecutorService singleThread = Executors.newSingleThreadExecutor();
    private List<Future<Boolean>> threadResults = new LinkedList<>();

    public HistoryController() throws HistoryControllerException {
        this.history = new LinkedList<>();
        initOutputStream();
        runThreadWaitingForWritingTasks();
    }

    public HistoryController(HistoryController history) throws HistoryControllerException {
        this.history = history.getHistory();
        initOutputStream();
        new Thread(this::runThreadWaitingForWritingTasks);
    }

    public List<Message> getHistory() throws HistoryControllerException {
        Message currentMessage;
        try (ObjectInputStream historyReader =
                new ObjectInputStream(
                    new FileInputStream(new File(".", historyFileName))
                )) {
            while ((currentMessage = (Message) historyReader.readObject()) != null) {
                history.add(currentMessage);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new HistoryControllerException("Couldn't get history", e);
        }
        return history;
    }

    public void addMessage(Message message) throws HistoryControllerException {
        try {
            taskQueue.put(() -> {
                try {
                    historyWriter.writeObject(message);
                } catch (IOException e) {
                    return false;
                }
                return true;
            });
        } catch (InterruptedException e) {
            throw new HistoryControllerException("Couldn't add message", e);
        }
    }

    private void initOutputStream() throws HistoryControllerException {
        try {
            historyWriter =
                    new ObjectOutputStream(
                            new FileOutputStream(
                                    new File(".", historyFileName),
                                    true
                            )
                    );
        } catch (IOException e) {
            throw new HistoryControllerException("Couldn't init history", e);
        }
    }

    private void runThreadWaitingForWritingTasks() {
        threadResults.add(singleThread.submit(() -> {
            while (!currentThread().isInterrupted()) {
                try {
                    if (!taskQueue.take().call()) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
            return true;
        }));
    }
}