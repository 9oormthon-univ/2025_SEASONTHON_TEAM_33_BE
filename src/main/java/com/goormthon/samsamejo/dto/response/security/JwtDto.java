package com.goormthon.samsamejo.dto.response.security;

public record JwtDto(
        String accessToken,
        Long accessTokenExpirationTime,
        String refreshToken,
        Long refreshTokenExpirationTime
) {
}
