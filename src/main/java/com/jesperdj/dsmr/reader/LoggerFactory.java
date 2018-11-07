package com.jesperdj.dsmr.reader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoggerFactory {

    private static final Map<Class<?>, Logger> LOGGERS = new ConcurrentHashMap<>();

    private LoggerFactory() {
    }

    public static Logger getLogger(Class<?> cls) {
        return LOGGERS.computeIfAbsent(cls, c -> new ConsoleLogger());
    }
}
