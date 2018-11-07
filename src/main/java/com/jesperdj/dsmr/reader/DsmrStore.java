package com.jesperdj.dsmr.reader;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DsmrStore implements Consumer<DsmrMessage> {
    private static final Logger log = LoggerFactory.getLogger(DsmrStore.class);

    private final Deque<DsmrMessage> queue = new ConcurrentLinkedDeque<>();

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;

    public void startup() {
        log.info("DsmrStore startup");
        future = scheduledExecutorService.scheduleWithFixedDelay(this::saveMessages, 5L, 5L, TimeUnit.MINUTES);
    }

    public void shutdown() {
        future.cancel(false);
        scheduledExecutorService.shutdown();
        try {
            if (!scheduledExecutorService.awaitTermination(30, TimeUnit.SECONDS)) {
                log.warn("Executor service failed to shutdown");
                scheduledExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.warn("InterruptedException while awaiting shutdown of executor service");
        }

        saveMessages();
        log.info("DsmrStore shutdown");
    }

    @Override
    public void accept(DsmrMessage message) {
        queue.addLast(message);
    }

    private void saveMessages() {
        int count = 0;

        String lastFilename = null;
        BufferedWriter out = null;

        DsmrMessage message;
        while ((message = queue.pollFirst()) != null) {
            String filename = String.format("%1$tY%1$tm%1$td.dsmr", message.getDateTime());

            if (!filename.equals(lastFilename)) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        log.warn("Exception while closing file: %s - %s", lastFilename, e.getMessage());
                    }
                }

                try {
                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), StandardCharsets.US_ASCII));
                    log.info("Writing to file: %s", filename);
                } catch (IOException e) {
                    log.error("Exception while opening file: %s - %s", filename, e.getMessage());
                    queue.addFirst(message);
                    return;
                }

                lastFilename = filename;
            }

            try {
                for (String record : message.getRecords()) {
                    out.write(record);
                }
            } catch (IOException e) {
                log.warn("Exception while writing message to file: %s - %s", filename, e.getMessage());
            }

            count++;
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                log.warn("Exception while closing file: %s - %s", lastFilename, e.getMessage());
            }
        }

        log.info("Saved %d messages", count);
    }
}
