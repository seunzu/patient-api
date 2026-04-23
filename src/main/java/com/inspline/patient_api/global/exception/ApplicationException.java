package com.inspline.patient_api.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;

    public ApplicationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.httpStatus = errorCode.getHttpStatus();
    }

    public ApplicationException(ErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.httpStatus = errorCode.getHttpStatus();
    }
}