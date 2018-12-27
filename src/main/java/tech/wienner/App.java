package tech.wienner;

import tech.wienner.client.Client;
import tech.wienner.client.ConsoleView;
import tech.wienner.client.View;
import tech.wienner.common.Message;
import tech.wienner.common.Sender;
import tech.wienner.server.MemoryPersister;
import tech.wienner.server.Persister;
import tech.wienner.server.Server;

import java.util.Date;

public class App {
    public static void main(String[] args) {
        Persister persister = new MemoryPersister();
        Server server = new Server(persister);
        View view = new ConsoleView();

        Client client1 = new Client(server, view);
        Client client2 = new Client(server, view);

        Sender sender1 = new Sender("Sender 1");
        Sender sender2 = new Sender("Sender 2");

        Date date1 = new Date(System.currentTimeMillis());
        Message helloMessage = new Message("Hello", date1, sender1);

        Date date2 = new Date(System.currentTimeMillis());
        Message byeMessage = new Message("Bye", date2, sender2);

        client1.sendMessage(helloMessage);
        client2.sendMessage(byeMessage);
    }
}
