package com.goormthon.samsamejo.constant;

import java.util.List;

public class Constant {

    public static final List<String> PERMIT_ALL_URLS = List.of(
            "/api/v1/oauth2/**",

            "/swagger",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs.html",
            "/api-docs",
            "/api-docs/**",
            "/swagger-ui",
            "/v3/**"
    );
}
