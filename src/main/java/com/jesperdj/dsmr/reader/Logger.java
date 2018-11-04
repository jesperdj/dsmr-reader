package com.jesperdj.dsmr.reader;

public interface Logger {
    enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    void log(Level level, String msg, Object... args);

    default void trace(String msg, Object... args) {
        log(Level.TRACE, msg, args);
    }

    default void debug(String msg, Object... args) {
        log(Level.DEBUG, msg, args);
    }

    default void info(String msg, Object... args) {
        log(Level.INFO, msg, args);
    }

    default void warn(String msg, Object... args) {
        log(Level.WARN, msg, args);
    }

    default void error(String msg, Object... args) {
        log(Level.ERROR, msg, args);
    }
}
