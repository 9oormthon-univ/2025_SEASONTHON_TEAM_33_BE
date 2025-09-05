package com.goormthon.samsamejo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
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
        setHeader(response);
        Map<String, Object> responseBody = Map.of(
                "status", status,
                "code", code,
                "message", message,
                "data", data
        );
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    public static void setHeader(HttpServletResponse response) {
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.setCharacterEncoding(RESPONSE_CHARACTER_ENCODING);
    }
}
