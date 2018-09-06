package com.db.chat;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ClientTest {
    private Client client;
    private Server mockServer;
    private View mockView;
    private Message mockMessage;
/*
    @Before
    public void setup() {
        mockServer = mock(Server.class);
        mockView = mock(View.class);
        mockMessage = mock(Message.class);

        client = new Client(mockServer, mockView);
    }

    @Test
    public void sendTest() {
        client.send(mockMessage);

        verify(mockServer, times(1)).receive(mockMessage);
    }

    @Test
    public void receiveTest() {
        client.receive(mockMessage);

        verify(mockView, times(1)).display(mockMessage);
    }

    @Test
    public void historyTest() {
        ArrayList<Message> history = new ArrayList<Message>(1);
        when(mockServer.getHistory()).thenReturn(history);
        client.getHistory();

        verify(mockServer, times(1)).getHistory();
        verify(mockView, times(1)).displayHistory(history);
    }*/
}
