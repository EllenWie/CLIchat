package com.db.chat;

import com.db.chat.client.Client;
import com.db.chat.core.Message;
import com.db.chat.server.ClientSession;
import com.db.chat.server.history.HistoryController;
import com.db.chat.server.history.HistoryControllerException;
import com.db.chat.server.Server;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ServerTest {
    private Server server;
    private Client mockClient;
    private ClientSession mockClientSession;
    private Message mockMessage;
    private HistoryController mockHistory;

    @Before
    public void setup() {
        mockHistory = mock(HistoryController.class);
        server = new Server(mockHistory);
        mockClient = mock(Client.class);
        mockClientSession = mock(ClientSession.class);
        mockMessage = mock(Message.class);
    }

    @Test
    public void shouldAddHistoryWhenServerReceive() throws HistoryControllerException {
        server.receive(mockMessage);

        verify(mockHistory, times(1)).addMessage(mockMessage);
    }

//    @Test
//    public void shouldCallClientsReceiveWhenServerReceive() {
//        //server.connect(mockClient);
//        server.receive(mockMessage);
//
//        verify(server, times(1)).send(mockMessage);
//    }

//    public void shouldGetHistoryWhenInvokeMethod() {
//        server.getHistory(mockClientSession);
//
//        verify(server, times(1)).send(mockMessage);
//        client.sendMessage(serializeMessage(message));
//    }
}
