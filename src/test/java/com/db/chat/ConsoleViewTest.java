package com.db.chat;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ConsoleViewTest {
    ConsoleView consoleView;
    Client client;

    @Before
    public void setup() {
        consoleView = new ConsoleView();
        client = mock(Client.class);
    }

    @Test
    public void shouldSendMessageWhenDo() {
        consoleView.send("/snd Hello, world");

    }

    @Test
    public void shouldShowHistoryWhenDo() {
        consoleView.send("/hist");
    }

//    @Test
//    public void shouldQuitClientWhenDo() {
//        consoleView.send("/quit");
//    }
}
