package com.goormthon.samsamejo.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class HttpHeaderUtil {

    public static Optional<String> getHeaderValue(HttpServletRequest request, String header, String prefix) {
        String value = request.getHeader(header);

        if (!StringUtils.hasText(value) || !value.startsWith(prefix)) {
            return Optional.empty();
        }
        return Optional.of(value.substring(prefix.length()));
    }
}
