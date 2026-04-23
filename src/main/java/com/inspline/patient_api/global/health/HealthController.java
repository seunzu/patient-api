package com.inspline.patient_api.global.health;

import com.inspline.patient_api.global.health.dto.HealthResponse;
import com.inspline.patient_api.global.shutdown.ShutdownManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final ShutdownManager shutdownManager;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        if (shutdownManager.isShuttingDown()) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new HealthResponse(
                            "shutting_down",
                            "disconnected",
                            shutdownManager.getUptime()));
        }

        return ResponseEntity.ok(
                new HealthResponse(
                        "healthy",
                        "connected",
                        shutdownManager.getUptime()));
    }

}
