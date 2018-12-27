package tech.wienner.server;

import tech.wienner.common.Message;

import java.util.ArrayList;

public class DatabasePersiter implements Persister {
    @Override
    public void write(Message message) {

    }

    @Override
    public ArrayList<Message> read() {
        return new ArrayList<>();
    }

    @Override
    public void cleanup() {

    }
}
