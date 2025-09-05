package com.goormthon.samsamejo.security.exception;

import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.util.HttpResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("customAuthenticationEntryPoint called");
        ErrorCode errorCode = (ErrorCode) Optional.ofNullable(request.getAttribute("exception")).orElse(ErrorCode.NOT_FOUND_END_POINT);
        HttpResponseUtil.setErrorResponse(response, errorCode);
    }
}
