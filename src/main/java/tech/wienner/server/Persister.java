package tech.wienner.server;

import tech.wienner.common.Message;

import java.util.ArrayList;

public interface Persister {

    void write(Message message);

    ArrayList<Message> read();

    void cleanup();
}
