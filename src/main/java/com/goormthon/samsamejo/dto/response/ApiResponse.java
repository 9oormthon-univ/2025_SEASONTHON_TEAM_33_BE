package com.goormthon.samsamejo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;   // OK / ERROR
    private int code;        // HTTP status code
    private String message;  // 설명 메시지
    private T data;          // 실제 응답 데이터

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>("OK", 200, "요청에 성공했습니다.", data);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>("ERROR", code, message, null);
    }
}
