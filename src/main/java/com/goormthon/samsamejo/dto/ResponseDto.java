package com.goormthon.samsamejo.dto;

import com.goormthon.samsamejo.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDto<T> {

    private final HttpStatus status;
    private final int code;
    private final String message;
    private final T data;

    @Builder
    private ResponseDto(HttpStatus status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseDto<T> ok(T data) {
        return ResponseDto.<T>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message("요청에 성공했습니다.")
                .data(data)
                .build();
    }

    public static <T> ResponseDto<T> error(ErrorCode errorCode) {
        return ResponseDto.<T>builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(null)
                .build();
    }
}
