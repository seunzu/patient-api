package com.inspline.patient_api.patient.exception;

import com.inspline.patient_api.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PatientErrorCode implements ErrorCode {

    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "환자를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}