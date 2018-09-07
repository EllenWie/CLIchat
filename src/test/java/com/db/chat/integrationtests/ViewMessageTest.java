package com.db.chat.integrationtests;

import com.db.chat.ConsoleView;
import com.db.chat.Message;
import com.db.chat.MessageType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;


public class ViewMessageTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

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
        Message message = new Message(System.currentTimeMillis(), "Hello", MessageType.MESSAGE);
        ConsoleView consoleView = new ConsoleView();
        consoleView.display(message);
        assertThat(outContent.toString(), containsString("Hello"));
    }

}
