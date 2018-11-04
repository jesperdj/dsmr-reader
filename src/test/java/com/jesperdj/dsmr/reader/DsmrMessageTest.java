package com.jesperdj.dsmr.reader;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DsmrMessageTest {

    @Test
    public void testGetDateTime() {
        DsmrMessage message = new DsmrMessage(Collections.singletonList("0-0:1.0.0(181104123918W)\r\n"));
        assertThat(message.getDateTime()).isEqualTo(LocalDateTime.of(2018, 11, 4, 12, 39, 18));
    }
}
