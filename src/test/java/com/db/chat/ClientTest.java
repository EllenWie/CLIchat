package com.db.chat;

import com.db.chat.client.Client;
import com.db.chat.client.ServerHelper;
import com.db.chat.client.view.View;
import com.db.chat.core.Chat;
import com.db.chat.core.Message;
import org.junit.Before;
import org.junit.Test;

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
