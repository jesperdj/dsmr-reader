package com.jesperdj.dsmr.reader;

import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.DataBits;
import com.pi4j.io.serial.FlowControl;
import com.pi4j.io.serial.Parity;
import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialConfig;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.StopBits;

import java.io.IOException;

public class DsmrReader {
    public static final Logger log = new ConsoleLogger();

    private static final String DEFAULT_DEVICE = "/dev/ttyUSB0";

    public static void main(String[] args) throws InterruptedException {
        log.info("DSMR Reader");

        String device = args.length > 0 ? args[0] : DEFAULT_DEVICE;
        log.debug("Using device: %s", device);

        DsmrStore store = new DsmrStore();
        DsmrParser parser = new DsmrParser(store);

        Serial serial = SerialFactory.createInstance();
        serial.addListener(event -> {
            try {
                parser.parse(event.getAsciiString());
            } catch (IOException e) {
                log.warn("Error while receiving data: %s", e.getMessage());
                parser.reset();
            }
        });

        SerialConfig config = new SerialConfig();
        config.device(device)
                .baud(Baud._115200)
                .dataBits(DataBits._8).parity(Parity.NONE).stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

        try {
            log.debug("Opening connection");
            serial.open(config);
        } catch (IOException e) {
            log.error("Error opening connection to device: %s - %s", device, e.getMessage());
            System.exit(1);
        }

        log.info("Connection opened, receiving messages");

        // Wait forever
        Thread.currentThread().join();
    }
}
