package tech.wienner;

import java.util.UUID;

public class Sender {
    private String name;
    private String id;

    public Sender(String name) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(name)
                .toString();
    }
}
