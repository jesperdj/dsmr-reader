package com.jesperdj.dsmr.reader;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DsmrParserTest {

    public interface DsmrMessageConsumer extends Consumer<DsmrMessage> {
    }

    @Test
    public void testParseMessage() {
        Consumer<DsmrMessage> messageConsumer = mock(DsmrMessageConsumer.class);
        DsmrParser parser = new DsmrParser(messageConsumer);

        parser.accept("/ISK5\\2M550T-1012\r\n");
        parser.accept("\r\n");
        parser.accept("1-3:0.2.8(50)\r\n");
        parser.accept("!9731\r\n");

        ArgumentCaptor<DsmrMessage> messageCaptor = ArgumentCaptor.forClass(DsmrMessage.class);
        verify(messageConsumer).accept(messageCaptor.capture());

        DsmrMessage message = messageCaptor.getValue();
        assertThat(message.getRecords()).containsExactly(
                "/ISK5\\2M550T-1012\r\n",
                "\r\n",
                "1-3:0.2.8(50)\r\n",
                "!9731\r\n");

        verifyNoMoreInteractions(messageConsumer);
    }
}
