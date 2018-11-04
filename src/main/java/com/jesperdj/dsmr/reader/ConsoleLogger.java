package com.jesperdj.dsmr.reader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements Logger {

    private static final DateTimeFormatter LOG_TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void log(Level level, String msg, Object... args) {
        System.out.printf("[%s] %-5s %s%n", LOG_TIMESTAMP_FORMATTER.format(LocalDateTime.now()), level, String.format(msg, args));
    }
}
