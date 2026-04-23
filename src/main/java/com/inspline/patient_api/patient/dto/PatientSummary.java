package com.inspline.patient_api.patient.dto;

public record PatientSummary(
        String id,
        String name,
        String dateOfBirth
) {}