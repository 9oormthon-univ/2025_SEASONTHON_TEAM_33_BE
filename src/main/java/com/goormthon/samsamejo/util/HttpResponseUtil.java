package com.goormthon.samsamejo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.goormthon.samsamejo.constant.Constant.*;

public class HttpResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> void setResponse(
            HttpServletResponse response,
            HttpStatus status,
            int code,
            String message,
            T data
    ) throws IOException {
        setHeader(response, status);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", status);
        responseBody.put("code", code);
        responseBody.put("message", message);
        responseBody.put("data", data);
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    public static void setHeader(HttpServletResponse response, HttpStatus status) {
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.setCharacterEncoding(RESPONSE_CHARACTER_ENCODING);
        response.setStatus(status.value());
    }
}
