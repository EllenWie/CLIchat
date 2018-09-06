package com.db.chat;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ServerTest {
    private Server server;
    private Client mockClient;
    private Message mockMessage;
    private History mockHistory;

    @Before
    public void setup() {
        mockHistory = mock(History.class);
        server = new Server(mockHistory);
        mockClient = mock(Client.class);
        mockMessage = mock(Message.class);
    }

    @Test
    public void shouldAddHistoryWhenServerReceive() {
        server.receive(mockMessage);

        verify(mockHistory, times(1)).addMessage(mockMessage);
    }

    @Test
    public void shouldCallClientsReceiveWhenServerReceive() {
        server.connect(mockClient);
        server.receive(mockMessage);

        verify(mockClient, times(1)).receive(mockMessage);
    }
}
