package com.jesperdj.dsmr.reader;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static com.jesperdj.dsmr.reader.DsmrReader.log;

public class DsmrStore implements Consumer<DsmrMessage> {

    @Override
    public void accept(DsmrMessage message) {
        String filename = String.format("%1$tY%1$tm%1$td.dsmr", message.getDateTime());
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), StandardCharsets.US_ASCII))) {
            for (String record : message.getRecords()) {
                out.write(record);
            }
        } catch (IOException e) {
            log.warn("Error writing message to file: %s", filename);
        }
    }
}
