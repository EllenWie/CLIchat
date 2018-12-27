package tech.wienner.client;

import tech.wienner.common.Message;
import tech.wienner.server.Server;

import java.util.UUID;

public class Client {
    private String clientId;
    private View view;
    private Server server;

    public Client(Server server, View view) {
        this.clientId = UUID.randomUUID().toString();
        this.view = view;
        this.server = server;
        server.addClient(this);
    }

    public String getClientId() {
        return clientId;
    }

    public void sendMessage(Message message) {
        server.receiveMessage(message);
    }

    public void receiveMessage(Message message){
        view.display(message);
    }
}
