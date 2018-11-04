package com.jesperdj.dsmr.reader;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jesperdj.dsmr.reader.DsmrReader.log;

public class DsmrParser {

    private static final Pattern START_OF_MESSAGE = Pattern.compile("/.{3}5.*\\r\\n\\r\\n");
    private static final Pattern END_OF_MESSAGE = Pattern.compile("!\\p{XDigit}\\r\\n");

    private final Consumer<String> messageConsumer;

    private final StringBuilder buffer = new StringBuilder(4096);

    private boolean messageStarted = false;

    public DsmrParser(Consumer<String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    public void parse(String data) {
        buffer.append(data);

        if (!messageStarted) {
            Matcher matcher = START_OF_MESSAGE.matcher(buffer.toString());
            if (matcher.find()) {
                int start = matcher.start();
                if (start > 0) {
                    // Discard any characters before the start of the message
                    log.warn("Discarding %d characters before start of message", start);
                    buffer.delete(0, start);
                }
                messageStarted = true;
            } else if (buffer.length() > 2048) {
                log.warn("Resetting buffer after reading %d characters without start of message", buffer.length());
                reset();
            }
        } else {
            Matcher matcher = END_OF_MESSAGE.matcher(buffer.toString());
            if (matcher.find()) {
                int end = matcher.end();
                messageConsumer.accept(buffer.substring(0, end));
                buffer.delete(0, end);
            }
        }
    }

    public void reset() {
        buffer.delete(0, buffer.length());
        messageStarted = false;
    }
}
