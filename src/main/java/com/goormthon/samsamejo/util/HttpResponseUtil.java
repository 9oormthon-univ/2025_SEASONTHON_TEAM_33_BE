package com.goormthon.samsamejo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goormthon.samsamejo.exception.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static com.goormthon.samsamejo.constant.Constant.*;

public class HttpResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        setHeader(response);
        Map<String, Object> responseBody = Map.of(
                "status", errorCode.getStatus(),
                "code", errorCode.getCode(),
                "message", errorCode.getMessage(),
                "data", Collections.emptyList()
        );
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    public static void setHeader(HttpServletResponse response) {
        response.setContentType(RESPONSE_CONTENT_TYPE);
        response.setCharacterEncoding(RESPONSE_CHARACTER_ENCODING);
    }
}
