package com.jesperdj.dsmr.reader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DsmrMessage {

    private static final Pattern DATE_TIME_RECORD = Pattern.compile("0-0:1.0.0\\((\\d{12})W\\)\\r\\n");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private final List<String> records = new ArrayList<>();

    public DsmrMessage(List<String> records) {
        this.records.addAll(records);
    }

    public List<String> getRecords() {
        return records;
    }

    public LocalDateTime getDateTime() {
        for (String record : records) {
            Matcher matcher = DATE_TIME_RECORD.matcher(record);
            if (matcher.matches()) {
                return LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
            }
        }

        return null;
    }
}
