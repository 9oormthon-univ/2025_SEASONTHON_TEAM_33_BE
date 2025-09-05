package com.goormthon.samsamejo.constant;

import java.util.List;

public class Constant {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String USER_ID_CLAIM_NAME = "uid";
    public static final String USER_ROLE_CLAIM_NAME = "rol";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    public static final String TOKEN_COOKIE_PATH = "/";

    public static final String RESPONSE_CONTENT_TYPE = "application/json";
    public static final String RESPONSE_CHARACTER_ENCODING = "UTF-8";

    public static final List<String> ADMIN_ONLY_URLS = List.of(
            "/api/v1/recruitments/crawl",
            "/api/v1/recruitments/status",

            "/api/v1/ai/**",

            "/swagger-ui/**",
            "/api-docs/**",
            "/v3/**"
    );

    public static final List<String> USER_AUTH_URLS = List.of(
            "/api/v1/auth/**",

            "/api/v1/recruitments/*/write",
            "/api/v1/recruitments/crawl",
            "/api/v1/recruitments/status",

            "/api/v1/user-recruitments/**",

            "/api/v1/archives/**"
    );

    public static final List<String> PERMIT_ALL_URLS = List.of(
            "/favicon.ico",

            "/login/oauth2/code/*",
            "/oauth2/authorization/*",

            "/api/v1/recruitments",
            "/api/v1/recruitments/*"
    );
}
