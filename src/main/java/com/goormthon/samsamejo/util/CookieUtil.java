package com.goormthon.samsamejo.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {

    public static Cookie createCookie(
            HttpServletResponse response,
            String name,
            String value,
            String domain,
            String path,
            int maxAge
    ) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
        return cookie;
    }

    public static Cookie createSecureCookie(
            HttpServletResponse response,
            String name,
            String value,
            String domain,
            String path,
            int maxAge
    ) {
        Cookie cookie = new Cookie(name, value);

        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
        return cookie;
    }

    public static Optional<String> getCookieValue(HttpServletRequest request, String name) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .map(Cookie::getValue);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(name))
                .forEach(cookie -> {
                    Cookie deleteCookie = new Cookie(name, null);
                    Optional.ofNullable(cookie.getDomain()).ifPresent(deleteCookie::setDomain);
                    deleteCookie.setPath(cookie.getPath());
                    deleteCookie.setHttpOnly(cookie.isHttpOnly());
                    deleteCookie.setSecure(cookie.getSecure());
                    deleteCookie.setMaxAge(0);
                    response.addCookie(deleteCookie);
                });
    }
}
