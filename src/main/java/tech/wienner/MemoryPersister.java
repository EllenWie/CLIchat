package tech.wienner;

import java.util.ArrayList;

public class MemoryPersister implements Persister {
    ArrayList<Message> storage;

    public MemoryPersister() {
        storage = new ArrayList<>();
    }

    public MemoryPersister(ArrayList<Message> storage) {
        this.storage = storage;
    }

    @Override
    public void write(Message message) {
        storage.add(message);
    }

    @Override
    public ArrayList<Message> read() {
        return storage;
    }

    @Override
    public void cleanup() {
        storage.clear();
    }
}
