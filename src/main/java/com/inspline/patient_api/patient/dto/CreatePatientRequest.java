package com.inspline.patient_api.patient.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePatientRequest(
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @NotBlank(message = "생년월일은 필수입니다.")
        String dateOfBirth,

        String phone,
        String insuranceNumber
) {}