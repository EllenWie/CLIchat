package com.db.chat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;

public class MessageTest {
    private Message message;
    private Date minDate;

    @Before
    public void setup() {
        message = new Message(Long.MIN_VALUE, "THIS", MessageType.MESSAGE);
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
