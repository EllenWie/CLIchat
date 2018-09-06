package com.db.chat.integrationtests;

import com.db.chat.Client;
import com.db.chat.Message;
import com.db.chat.Server;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
