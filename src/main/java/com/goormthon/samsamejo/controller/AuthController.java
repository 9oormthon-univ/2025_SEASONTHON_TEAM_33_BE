package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.annotation.UserId;
import com.goormthon.samsamejo.constant.Constant;
import com.goormthon.samsamejo.dto.ResponseDto;
import com.goormthon.samsamejo.dto.request.auth.UserInfoRequest;
import com.goormthon.samsamejo.dto.response.security.JwtDto;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.service.AuthService;
import com.goormthon.samsamejo.util.CookieUtil;
import com.goormthon.samsamejo.util.HttpResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.goormthon.samsamejo.constant.Constant.ACCESS_TOKEN;
import static com.goormthon.samsamejo.constant.Constant.REFRESH_TOKEN;
import static com.goormthon.samsamejo.constant.Constant.TOKEN_COOKIE_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    @Value("${jwt.cookie-domain}")
    private String cookieDomain;
    @Value("${spring.security.login.success-redirect-uri}")
    private String redirectUri;

    @GetMapping("/reissue")
    public void reissue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = CookieUtil.getCookieValue(request, Constant.REFRESH_TOKEN)
                .orElseThrow(() -> new RestException(ErrorCode.NO_REFRESH_TOKEN_HEADER));

        JwtDto jwtDto = authService.reissue(refreshToken);
        HttpResponseUtil.setHeader(response);
        CookieUtil.createCookie(response, ACCESS_TOKEN, jwtDto.accessToken(), cookieDomain, TOKEN_COOKIE_PATH, jwtDto.accessTokenExpirationTime().intValue());
        CookieUtil.createSecureCookie(response, REFRESH_TOKEN, jwtDto.refreshToken(), cookieDomain, TOKEN_COOKIE_PATH, (int) (jwtDto.refreshTokenExpirationTime() / 1000));
        response.sendRedirect(redirectUri);
    }

    @PostMapping("/user-info")
    public ResponseDto<Void> craeteUserInfo(@UserId Long userId, @RequestBody UserInfoRequest userInfoRequest) {
        authService.createUserInfo(
                userId,
                userInfoRequest.name(),
                userInfoRequest.email(),
                userInfoRequest.phoneNumber(),
                userInfoRequest.education(),
                userInfoRequest.major(),
                userInfoRequest.interestFields(),
                userInfoRequest.skills()
        );
        return ResponseDto.ok(null);
    }
}
