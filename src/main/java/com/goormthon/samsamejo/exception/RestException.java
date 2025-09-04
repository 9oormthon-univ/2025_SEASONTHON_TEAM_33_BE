package com.goormthon.samsamejo.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestException extends RuntimeException {
    private final ErrorCode errorCode;
}
