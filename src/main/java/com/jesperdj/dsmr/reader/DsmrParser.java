package com.jesperdj.dsmr.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class DsmrParser implements Consumer<String> {

    private static final Pattern START_OF_MESSAGE = Pattern.compile("/.{3}5.*\\r\\n");
    private static final Pattern END_OF_MESSAGE = Pattern.compile("!\\p{XDigit}{4}\\r\\n");

    private final Consumer<DsmrMessage> messageConsumer;

    private final List<String> messageLines = new ArrayList<>();

    private boolean messageStarted = false;

    public DsmrParser(Consumer<DsmrMessage> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void accept(String line) {
        if (!messageStarted) {
            if (START_OF_MESSAGE.matcher(line).matches()) {
                messageLines.add(line);
                messageStarted = true;
            }
        } else {
            messageLines.add(line);
            if (END_OF_MESSAGE.matcher(line).matches()) {
                messageConsumer.accept(new DsmrMessage(messageLines));
                messageLines.clear();
                messageStarted = false;
            }
        }
    }
}
