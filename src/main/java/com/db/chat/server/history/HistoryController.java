package com.db.chat.server.history;

import com.db.chat.core.Message;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.currentThread;


public class HistoryController {
    private List<Message> history;
    private BlockingQueue<Callable<Boolean>> taskQueue = new LinkedBlockingQueue<>();
    private String historyFileName = "history.txt";
    private ObjectOutputStream historyWriter;
    private ExecutorService singleThread = Executors.newSingleThreadExecutor();
    private List<Future<Boolean>> threadResults = new LinkedList<>();

    public HistoryController() throws HistoryControllerException {
        this.history = new LinkedList<>();
        initOutputStream();
        runThreadWaitingForWritingTasks();
    }

    public HistoryController(String historyFileName) throws HistoryControllerException {
        this.historyFileName = historyFileName;
        this.history = new LinkedList<>();
        initOutputStream();
        runThreadWaitingForWritingTasks();
    }

    public HistoryController(HistoryController history) throws HistoryControllerException {
        this.history = history.getHistory();
        initOutputStream();
        runThreadWaitingForWritingTasks();
    }

    public List<Message> getHistory() throws HistoryControllerException {
        Message currentMessage;
        history.clear();
        try (ObjectInputStream historyReader =
                new ObjectInputStream(
                    new FileInputStream(
                            new File(".", historyFileName)
                    )
                )
        ) {
            while ((currentMessage = (Message) historyReader.readObject()) != null) {
                history.add(currentMessage);
            }
        } catch (EOFException e) {
            return history;
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
                    historyWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            });
        } catch (InterruptedException e) {
            throw new HistoryControllerException("Couldn't add message", e);
        }
    }

    private void initOutputStream() throws HistoryControllerException {
        File historyFile = new File(".", historyFileName);
        if (historyFile.exists()) {
            try {
                historyWriter =
                        new AppendingObjectOutputStream(
                                new FileOutputStream(
                                        new File(".", historyFileName),
                                        true
                                )
                        );
            } catch (IOException e) {
                throw new HistoryControllerException("Couldn't init history", e);
            }
        } else {
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

class AppendingObjectOutputStream extends ObjectOutputStream {
    public AppendingObjectOutputStream(FileOutputStream out) throws IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws IOException {
        reset();
    }

}