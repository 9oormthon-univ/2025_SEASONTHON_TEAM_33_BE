package com.goormthon.samsamejo.security.exception;

import com.goormthon.samsamejo.util.HttpResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.goormthon.samsamejo.exception.ErrorCode.*;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("customAccessDeniedHandler called");
        HttpResponseUtil.setResponse(response, ACCESS_DENIED.getStatus(), ACCESS_DENIED.getCode(), ACCESS_DENIED.getMessage(), null);
    }
}
