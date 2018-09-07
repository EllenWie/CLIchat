package com.db.chat.integrationtests;

import com.db.chat.client.Client;
import com.db.chat.core.Message;
import com.db.chat.server.Server;

import static org.mockito.Mockito.mock;

public class ClientViewTest {
    private Server server;
    private Message message;
    private Client client;
/*
    @Before
    public void setup() {
        server = mock(Server.class);
        message = mock(Message.class);
    }

    @Test
    public void shouldPrintMessage() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                client.send(message);
                return null;
            }
        }).when(server).receive(message);
        client = new Client(server);
    }*/
}
