package com.db.chat.integrationtests;

import com.db.chat.Client;
import com.db.chat.ConsoleView;
import com.db.chat.Message;
import com.db.chat.MessageType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.lang.System.currentTimeMillis;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class ViewMessageTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private ConsoleView consoleView = new ConsoleView();
    private Client mockClient = mock(Client.class);

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void shouldDisplayMessageContentWhenInvokeDisplay() {
        Message message = new Message(currentTimeMillis(), "Hello", MessageType.MESSAGE);

        consoleView.display(message);
        assertThat(outContent.toString(), containsString("Hello"));
    }

//    @Test
//    public void shouldSendMessageWhenDo() {
//        String command = "/snd Hello, world";
//
//        String text = command.substring(5);
//        Message message = new Message(currentTimeMillis(), text, MessageType.MESSAGE);
//        consoleView.send(command);
//        verify(mockClient, times(1)).send(message);
//
//    }

//    @Test
//    public void shouldShowHistoryWhenDo() {
//        consoleView.send("/hist");
//    }

//    @Test
//    public void shouldQuitClientWhenDo() {
//        consoleView.send("/quit");
//    }

}
