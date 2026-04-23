package com.inspline.patient_api.auth.exception;

import com.inspline.patient_api.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.FORBIDDEN, "토큰이 유효하지 않습니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "토큰이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
