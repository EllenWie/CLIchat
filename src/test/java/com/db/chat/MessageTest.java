package com.db.chat;

import com.db.chat.core.Message;
import com.db.chat.core.MessageType;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MessageTest {
    private Message message;
    private Date minDate;

    @Before
    public void setup() {
        //TODO: nick added. if necesery, set nick and check
        message = new Message(Long.MIN_VALUE, "THIS", MessageType.MESSAGE, null);
        minDate = new Date(Long.MIN_VALUE);
    }

    @Test
    public void shouldChangeDateWhenInvokeSet() {
        Message nullMessage = new Message();
        nullMessage.setTime(minDate);

        assertEquals(minDate, nullMessage.getTime());
        assertEquals(minDate, message.getTime());
    }

    @Test
    public void shouldFormatMessageWhenInvokeToString() {
        String formatedString = message.toString();
        assertEquals(minDate.toString() + ": THIS", formatedString);
    }
}
