package com.db.chat;

import java.util.ArrayList;
import java.util.Date;

public class Server implements ServerInterface {
    private HistoryController history;
    private ArrayList<Client> clients;

    public Server() {
        try {
            history = new HistoryController();
        } catch (HistoryControllerException e) {
            e.printStackTrace();
        }
        clients = new ArrayList<>();
    }

    public Server(HistoryController history) {
        this.history = history;
        clients = new ArrayList<>();
    }

    public Server(HistoryController history, ArrayList<Client> clients) {
        this.history = history;
        this.clients = clients;
    }

    public void receive(Message message) {
        message.setTime(new Date());
        try {
            this.history.addMessage(message);
        } catch (HistoryControllerException e) {
            e.printStackTrace();
        }
        this.send(message);
    }

    public void send(Message message) {
        for (Client client : this.clients) {
            client.receive(message);
        }
    }

    public void getHistory() {
        //send(this.history.getHistory();)
    }

    public ArrayList<Client> getClients() {
        return clients;
    }

    //TODO: store sockets instead of Clients and do it in accept without function connect
    public void connect(Client client) {
        clients.add(client);
    }
}
