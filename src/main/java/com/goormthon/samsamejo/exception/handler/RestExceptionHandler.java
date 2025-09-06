package com.goormthon.samsamejo.exception.handler;

import com.goormthon.samsamejo.dto.ResponseDto;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    // ==================== 400 BAD REQUEST ====================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseDto<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseDto<?> handleHandlerMethodValidationException(HandlerMethodValidationException e) {
        log.error("HandlerMethodValidationException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseDto<?> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("ConstraintViolationException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseDto<?> handleServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseDto<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<?> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.BAD_REQUEST);
    }

    // ==================== 500 INTERNAL SERVER ERROR ====================

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseDto<?> handleUnexpectedTypeException(UnexpectedTypeException e) {
        log.error("UnexpectedTypeException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseDto<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDto<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseDto<?> handleException(Exception e) {
        log.error("Exception raised: {}", e.getMessage(), e);

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseDto<?> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException raised: {}", e.getMessage());
        return ResponseDto.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    // ==================== Custom RestException ====================

    @ExceptionHandler(RestException.class)
    public ResponseDto<?> handleRestException(RestException e) {
        log.error("RestException raised: {}", e.getErrorCode().getMessage());
        return ResponseDto.error(e.getErrorCode());
    }
}
