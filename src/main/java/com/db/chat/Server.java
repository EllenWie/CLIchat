package com.db.chat;

import java.util.ArrayList;
import java.util.Date;

public class Server {
    private History history;
    private ArrayList<Client> clients;

    public Server() {
        history = new History();
        clients = new ArrayList<>();
    }

    public Server(History history) {
        this.history = history;
        clients = new ArrayList<>();
    }

    public Server(History history, ArrayList<Client> clients) {
        this.history = history;
        this.clients = clients;
    }

    public void receive(Message message) {
        message.setTime(new Date());
        this.history.addMessage(message);
        this.send(message);
    }

    void send(Message message) {
        for (Client client : this.clients) {
            client.receive(message);
        }
    }

    public ArrayList<Message> getHistory() {
        return this.history.getHistory();
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    public void connect(Client client) {
        clients.add(client);
    }
}
