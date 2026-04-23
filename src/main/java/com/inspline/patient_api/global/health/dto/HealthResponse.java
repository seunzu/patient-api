package com.inspline.patient_api.global.health.dto;

public record HealthResponse(
        String status,
        String db,
        long uptime
) {}