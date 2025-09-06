package com.goormthon.samsamejo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common Client Error
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 4000, "잘못된 요청 파라미터입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 4001, "입력값이 유효하지 않습니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, 4000, "반드시 pdf 파일을 업로드해야 합니다."),

    // Security Error
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, 4010, "접근이 거부된 토큰입니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, 4011, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 4012, "만료된 토큰입니다."),
    TOKEN_TYPE_ERROR(HttpStatus.UNAUTHORIZED, 4013, "토큰 타입 오류입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, 4014, "지원하지 않는 토큰입니다."),
    UNKNOWN_TOKEN(HttpStatus.UNAUTHORIZED, 4015, "알 수 없는 토큰 오류입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, 4016, "토큰 형식이 올바르지 않습니다."),
    SIGNATURE_FAILURE(HttpStatus.UNAUTHORIZED, 4017, "토큰의 서명 검증에 실패했습니다."),
    ILLEGAL_ARGUMENT(HttpStatus.UNAUTHORIZED, 4018, "토큰이 비어있습니다."),
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED, 4018, "로그인에 실패했습니다."),
    NO_REFRESH_TOKEN_HEADER(HttpStatus.UNAUTHORIZED, 4019, "헤더에 토큰이 없습니다."),
    NOT_REGISTERED_USER(HttpStatus.UNAUTHORIZED, 4020, "아직 추가 정보를 입력하지 않은 사용자입니다."),

    // Not Found Error
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 4040, "존재하지 않는 사용자입니다"),
    NOT_FOUND_FILE_PATH(HttpStatus.NOT_FOUND, 4041, "존재하지 않는 파일 경로입니다"),
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND, 4042, "존재하지 않는 요청 경로입니다."),
    NOT_FOUND_USER_RECRUITMENT(HttpStatus.NOT_FOUND, 4043, "존재하지 않는 자기소개서입니다."),
    NOT_FOUND_RECRUITMENT(HttpStatus.NOT_FOUND, 4003, "존재하지 않는 채용 공고입니다"),
    NOT_FOUND_QUESTION(HttpStatus.NOT_FOUND, 4004, "존재하지 않는 질문입니다"),

    // Permission Error
    FORBIDDEN(HttpStatus.FORBIDDEN, 4030, "권한이 없습니다."),

    // Server, File Up/DownLoad Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "서버 내부에 에러가 발생했습니다."),
    FILE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "파일 업로드에 실패했습니다."),
    FILE_DOWNLOAD(HttpStatus.INTERNAL_SERVER_ERROR, 5002, "파일 다운로드에 실패했습니다."),
    FILE_PROCESSING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 5003, "파일 바이트 처리 중 예외가 발생했습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}