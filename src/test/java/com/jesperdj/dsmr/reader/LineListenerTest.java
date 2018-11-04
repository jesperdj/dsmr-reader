package com.jesperdj.dsmr.reader;

import com.pi4j.io.serial.SerialDataEvent;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class LineListenerTest {

    public interface StringConsumer extends Consumer<String> {
    }

    @Test
    public void testReceiveSingleLine() throws IOException {
        Consumer<String> lineConsumer = mock(StringConsumer.class);
        LineListener lineListener = new LineListener(lineConsumer);

        SerialDataEvent event = mock(SerialDataEvent.class);
        when(event.getAsciiString()).thenReturn("test\r\n");
        lineListener.dataReceived(event);

        verify(lineConsumer).accept("test\r\n");
        verifyNoMoreInteractions(lineConsumer);
    }

    @Test
    public void testReceiveMultipleLines() throws IOException {
        Consumer<String> lineConsumer = mock(StringConsumer.class);
        LineListener lineListener = new LineListener(lineConsumer);

        SerialDataEvent event = mock(SerialDataEvent.class);
        when(event.getAsciiString()).thenReturn("first line\r\nsecond line\r\n");
        lineListener.dataReceived(event);

        verify(lineConsumer).accept("first line\r\n");
        verify(lineConsumer).accept("second line\r\n");
        verifyNoMoreInteractions(lineConsumer);
    }

    @Test
    public void testReceiveInParts() throws IOException {
        Consumer<String> lineConsumer = mock(StringConsumer.class);
        LineListener lineListener = new LineListener(lineConsumer);

        SerialDataEvent event1 = mock(SerialDataEvent.class);
        when(event1.getAsciiString()).thenReturn("abc");
        SerialDataEvent event2 = mock(SerialDataEvent.class);
        when(event2.getAsciiString()).thenReturn("\r\ndefg");
        SerialDataEvent event3 = mock(SerialDataEvent.class);
        when(event3.getAsciiString()).thenReturn("hij\r\nklmn");
        SerialDataEvent event4 = mock(SerialDataEvent.class);
        when(event4.getAsciiString()).thenReturn("\r\n");

        lineListener.dataReceived(event1);
        lineListener.dataReceived(event2);
        lineListener.dataReceived(event3);
        lineListener.dataReceived(event4);

        verify(lineConsumer).accept("abc\r\n");
        verify(lineConsumer).accept("defghij\r\n");
        verify(lineConsumer).accept("klmn\r\n");
        verifyNoMoreInteractions(lineConsumer);
    }
}
