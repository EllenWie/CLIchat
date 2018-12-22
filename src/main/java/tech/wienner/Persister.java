package tech.wienner;

import java.util.ArrayList;

public interface Persister {

    void write(Message message);

    ArrayList<Message> read();

    void cleanup();
}
