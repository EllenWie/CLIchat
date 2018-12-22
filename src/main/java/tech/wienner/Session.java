package tech.wienner;

import java.util.UUID;

public class Session {
    private Sender sender;
    private String sessionId;

    public Session(Sender sender) {
        this.sender = sender;
        this.sessionId = UUID.randomUUID().toString();
    }

    public Sender getSender() {
        return sender;
    }

    public String getSessionId() {
        return sessionId;
    }
}
