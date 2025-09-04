package com.goormthon.samsamejo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Not Found Error
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 4000, "존재하지 않는 사용자입니다"),
    NOT_FOUND_FILE_PATH(HttpStatus.NOT_FOUND, 4001, "존재하지 않는 파일 경로입니다"),

    // Security Error
    ACCESS_DENIED_ERROR(HttpStatus.UNAUTHORIZED, 4030, "접근이 거부된 토큰입니다."),
    TOKEN_INVALID_ERROR(HttpStatus.UNAUTHORIZED, 4031, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, 4032, "만료된 토큰입니다."),
    TOKEN_TYPE_ERROR(HttpStatus.UNAUTHORIZED, 4033, "토큰 타입 오류입니다."),
    TOKEN_UNSUPPORTED_ERROR(HttpStatus.UNAUTHORIZED, 4034, "지원하지 않는 토큰입니다."),
    TOKEN_UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, 4035, "알 수 없는 토큰 오류입니다."),
    TOKEN_MALFORMED_ERROR(HttpStatus.UNAUTHORIZED, 4036, "토큰 형식이 올바르지 않습니다."),

    // Server, File Up/DownLoad Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "서버 내부에 에러가 발생했습니다."),
    FILE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "파일 업로드에 실패했습니다."),
    FILE_DOWNLOAD(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "파일 다운로드에 실패했습니다."),
    FILE_PROCESSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "파일 바이트 처리 중 예외가 발생했습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}