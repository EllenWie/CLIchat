package tech.wienner.server;

import tech.wienner.common.Message;
import tech.wienner.client.Client;

import java.util.HashMap;
import java.util.Map;

public class Server {
    private Persister messagePersister;
    private Map<String, Client> clients;

    public Server(Persister messagePersister) {

        this.messagePersister = messagePersister;
        clients = new HashMap<>();
    }

    public void addClient(Client client) {
        String clientId = client.getClientId();
        this.clients.put(clientId, client);
    }

    public Client getClient(String id) {
        return clients.get(id);
    }

    public void removeClient(String id) {
        clients.remove(id);
    }

    public Map<String, Client> getClients() {
        return this.clients;
    }

    public void receiveMessage(Message message){
        messagePersister.write(message);
        sendMessage(message);
        System.out.println("Message was received - persisting to memory");
    }

    public void sendMessage(Message message){
        for (Client client : clients.values()) {
            client.receiveMessage(message);
        }
    }
}
