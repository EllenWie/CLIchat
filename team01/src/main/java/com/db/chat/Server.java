package com.db.chat;

import java.util.ArrayList;

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
        this.history.addMessage(message);
        this.send(message);
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    void send(Message message) {

    }

    public ArrayList<Message> getHistory() {
        return this.history.getHistory();
    }

    public void connect(Client client) {
        clients.add(client);
    }
}
