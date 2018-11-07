package com.jesperdj.dsmr.reader;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class DsmrParser implements Consumer<String> {

    private static final Pattern START_OF_MESSAGE = Pattern.compile("/.{3}5.*\\r\\n");
    private static final Pattern END_OF_MESSAGE = Pattern.compile("!\\p{XDigit}{4}\\r\\n");

    private final Consumer<DsmrMessage> messageConsumer;

    private DsmrMessage.Builder messageBuilder;

    public DsmrParser(Consumer<DsmrMessage> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public void accept(String line) {
        if (messageBuilder == null) {
            if (START_OF_MESSAGE.matcher(line).matches()) {
                messageBuilder = new DsmrMessage.Builder();
                messageBuilder.addRecord(line);
            }
        } else {
            messageBuilder.addRecord(line);
            if (END_OF_MESSAGE.matcher(line).matches()) {
                messageConsumer.accept(messageBuilder.build());
                messageBuilder = null;
            }
        }
    }
}
