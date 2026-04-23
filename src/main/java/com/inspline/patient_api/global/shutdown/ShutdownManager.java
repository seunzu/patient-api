package com.inspline.patient_api.global.shutdown;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShutdownManager {

    @Getter
    private volatile boolean shuttingDown = false;
    private final long startTime = System.currentTimeMillis();

    public void initiateShutdown() {
        log.info("[Shutdown] /health will now return 503.");
        this.shuttingDown = true;
    }

    public long getUptime() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}