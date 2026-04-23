package com.inspline.patient_api.global.shutdown;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShutdownLogger {

    private final ShutdownManager shutdownManager;

    @EventListener(ContextClosedEvent.class)
    public void onShutdown() {
        log.info("[Shutdown] Shutdown initiated.");
        shutdownManager.initiateShutdown();
        log.info("[Shutdown] Draining connections... (max 15s)");
    }

    @PreDestroy
    public void onClose() {
        log.info("[Shutdown] Closed.");
    }
}