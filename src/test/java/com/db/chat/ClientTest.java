package com.db.chat;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ClientTest {
    private Client client;
    private Chat mockChat;
    private View mockView;
    private Message mockMessage;

    @Before
    public void setup() {
        mockChat = mock(ServerHelper.class);
        mockView = mock(View.class);
        mockMessage = mock(Message.class);

        client = new Client(mockChat, mockView);
    }

    @Test
    public void sendTest() {
        client.send(mockMessage);

        verify(mockChat, times(1)).receive(mockMessage);
    }

    @Test
    public void receiveTest() {
        client.receive(mockMessage);

        verify(mockView, times(1)).display(mockMessage);
    }

}
