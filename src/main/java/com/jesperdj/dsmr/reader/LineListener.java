package com.jesperdj.dsmr.reader;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;

import java.io.IOException;
import java.util.function.Consumer;

public class LineListener implements SerialDataEventListener {
    private static final Logger log = LoggerFactory.getLogger(LineListener.class);


    private final Consumer<String> lineConsumer;

    private final StringBuilder builder = new StringBuilder(256);

    public LineListener(Consumer<String> lineConsumer) {
        this.lineConsumer = lineConsumer;
    }

    @Override
    public void dataReceived(SerialDataEvent event) {
        try {
            builder.append(event.getAsciiString());
        } catch (IOException e) {
            log.warn("Error while receiving data: %s", e.getMessage());
            builder.delete(0, builder.length());
            return;
        }

        int i;
        while ((i = builder.indexOf("\r\n")) != -1) {
            lineConsumer.accept(builder.substring(0, i + 2));
            builder.delete(0, i + 2);
        }
    }
}
