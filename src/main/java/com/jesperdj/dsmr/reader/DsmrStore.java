package com.jesperdj.dsmr.reader;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import static com.jesperdj.dsmr.reader.DsmrReader.log;

public class DsmrStore implements Consumer<DsmrMessage> {

    private long messageCount;

    private String lastFilename;
    private LocalDateTime lastLogTime;

    @Override
    public void accept(DsmrMessage message) {
        messageCount++;

        String filename = String.format("%1$tY%1$tm%1$td.dsmr", message.getDateTime());
        if (!filename.equals(lastFilename)) {
            log.info("Writing to file: %s", filename);
            lastFilename = filename;
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), StandardCharsets.US_ASCII))) {
            for (String record : message.getRecords()) {
                out.write(record);
            }
        } catch (IOException e) {
            log.warn("Error writing message to file: %s", filename);
        }

        LocalDateTime now = LocalDateTime.now();
        if (lastLogTime == null || lastLogTime.isBefore(now.minusMinutes(10))) {
            log.info("Stored %d messages", messageCount);
            lastLogTime = now;
        }
    }
}
