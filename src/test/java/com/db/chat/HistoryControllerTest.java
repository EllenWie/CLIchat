package com.db.chat;

import com.db.chat.core.Message;
import com.db.chat.core.MessageType;
import com.db.chat.server.history.HistoryController;
import com.db.chat.server.history.HistoryControllerException;
import org.junit.*;

import java.io.File;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class HistoryControllerTest {
    @BeforeClass
    public static void init() {
        File toBeDeleted = new File(".", "testFile.txt");
        if (toBeDeleted.exists()) {
            toBeDeleted.setWritable(true);
            toBeDeleted.delete();
        }
    }

    @Test
    public void shouldReturnCorrectListOfMessagesWhenGetHistory() throws HistoryControllerException, InterruptedException {
        HistoryController historyController = new HistoryController("testFile.txt");
        Message testMessage = new Message(new Date(), "Hey!", MessageType.MESSAGE);
        historyController.addMessage(testMessage);
        sleep(2000);
        List<Message> actualMessageList = historyController.getHistory();
        Message actualMessage = actualMessageList.get(0);

        assertEquals(testMessage.getText(), actualMessage.getText());
        assertEquals(testMessage.getType(), actualMessage.getType());
        assertEquals(testMessage.getTime(), actualMessage.getTime());
    }
}
