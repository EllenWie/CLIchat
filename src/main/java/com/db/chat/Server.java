package com.db.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Server implements ServerInterface{
    private History history;
    private ArrayList<Client> clients;

    public Server() {
        try {
            history = new History();
        } catch (HistoryException e) {
            e.printStackTrace();
        }
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
        try {
            this.history.addMessage(message);
        } catch (HistoryException e) {
            e.printStackTrace();
        }
        this.send(message);
    }

    public void send(Message message) {
        for (Client client : this.clients) {
            client.receive(message);
        }
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    //TODO: store sockets instead of Clients and do it in accept without function connect
    public void connect(Client client) {
        clients.add(client);
    }

    @Override
    public void close() throws IOException {
        //TODO here would be closing of server... maybe
    }
}
