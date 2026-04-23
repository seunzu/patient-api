package com.inspline.patient_api.patient.dto;

public record PatientDetail(
        String id,
        String name,
        String dateOfBirth,
        String phone,
        String insuranceNumber
) {}