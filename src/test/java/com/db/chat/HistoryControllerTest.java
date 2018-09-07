package com.db.chat;

import com.db.chat.client.Client;
import com.db.chat.core.Message;
import com.db.chat.core.MessageType;
import com.db.chat.server.Server;
import com.db.chat.server.history.HistoryController;
import com.db.chat.server.history.HistoryControllerException;
import org.junit.*;

import java.io.File;
import java.io.IOException;
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
        Message testMessage = new Message(new Date(), "Hey!", MessageType.MESSAGE, "сельский депутат");
        historyController.addMessage(testMessage);
        sleep(2000);
        List<Message> actualMessageList = historyController.getHistory();
        Message actualMessage = actualMessageList.get(0);

        assertEquals(testMessage.getText(), actualMessage.getText());
        assertEquals(testMessage.getType(), actualMessage.getType());
        assertEquals(testMessage.getTime(), actualMessage.getTime());
        assertEquals(testMessage.getNick(), actualMessage.getNick());
    }

    @Test
    public void shouldNotThrowExceptionWhenTwoThousandClients() throws HistoryControllerException, IOException, InterruptedException {
        Server testServer = new Server();
        new Thread(()->testServer.run()).start();
//        testServer.run();
        Client myClient = new Client("127.0.0.1", 6666);
        for (int i = 0; i < 5; ++i) {
//            for (int j = 0; j < 2; ++j) {
            myClient.send(new Message(new Date(), String.valueOf(i), MessageType.MESSAGE, String.valueOf(i)));
//            }

        }
        myClient.quit();
        sleep(5000);
        testServer.close();
    }
}
