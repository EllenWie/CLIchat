package com.db.chat;

import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class HistoryControllerTest {
    @Test
    public void shouldReturnCorrectListOfMessagesWhenGetHistory() throws HistoryControllerException, InterruptedException {
        HistoryController historyController = new HistoryController("testFile.txt");
        Message testMessage = new Message(new Date(), "Hey!", MessageType.MESSAGE);
        historyController.addMessage(testMessage);
        sleep(2000);
        List<Message> actualMessageList = historyController.getHistory();
//        new File("testFile.txt").delete();
        Message actualMessage = actualMessageList.get(0);

        assertEquals(testMessage.getText(), actualMessage.getText());
        assertEquals(testMessage.getType(), actualMessage.getType());
        assertEquals(testMessage.getTime(), actualMessage.getTime());
    }
}
