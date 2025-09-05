package com.goormthon.samsamejo.controller;

import com.goormthon.samsamejo.annotation.UserId;
import com.goormthon.samsamejo.annotation.ValidFile;
import com.goormthon.samsamejo.dto.ResponseDto;
import com.goormthon.samsamejo.dto.request.auth.CareerFileRequest;
import com.goormthon.samsamejo.dto.request.auth.UpdateUserInfoRequest;
import com.goormthon.samsamejo.dto.request.auth.UserInfoRequest;
import com.goormthon.samsamejo.dto.response.security.JwtDto;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.service.AuthService;
import com.goormthon.samsamejo.util.CookieUtil;
import com.goormthon.samsamejo.util.HttpResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.goormthon.samsamejo.constant.Constant.*;
import static com.goormthon.samsamejo.exception.ErrorCode.*;

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
        String refreshToken = CookieUtil.getCookieValue(request, REFRESH_TOKEN).orElseThrow(() -> new RestException(NO_REFRESH_TOKEN_HEADER));

        JwtDto jwtDto = authService.reissue(refreshToken);
        HttpResponseUtil.setHeader(response, HttpStatus.OK);
        CookieUtil.createCookie(response, ACCESS_TOKEN, jwtDto.accessToken(), cookieDomain, TOKEN_COOKIE_PATH, jwtDto.accessTokenExpirationTime().intValue());
        CookieUtil.createSecureCookie(response, REFRESH_TOKEN, jwtDto.refreshToken(), cookieDomain, TOKEN_COOKIE_PATH, (int) (jwtDto.refreshTokenExpirationTime() / 1000));
        response.sendRedirect(redirectUri);
    }

    @PostMapping("/user-info")
    public ResponseDto<Void> craeteUserInfo(
            @UserId Long userId,
            @Valid @RequestPart UserInfoRequest userInfoRequest,
            @ModelAttribute CareerFileRequest careerFileRequest
    ) throws IOException {
        authService.createUserInfo(
                userId,
                userInfoRequest.name(),
                userInfoRequest.email(),
                userInfoRequest.phoneNumber(),
                userInfoRequest.education(),
                userInfoRequest.major(),
                userInfoRequest.interestFields(),
                userInfoRequest.skills(),
                careerFileRequest.resume(),
                careerFileRequest.portfolio()
        );
        return ResponseDto.ok(null);
    }

    @PutMapping("/user-info")
    public ResponseDto<Void> updateUserInfo(@UserId Long userId, @Valid @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        authService.updateUserInfo(
                userId,
                updateUserInfoRequest.name(),
                updateUserInfoRequest.email(),
                updateUserInfoRequest.phoneNumber(),
                updateUserInfoRequest.education(),
                updateUserInfoRequest.major(),
                updateUserInfoRequest.interestFields(),
                updateUserInfoRequest.skills()
        );
        return ResponseDto.ok(null);
    }

    @PutMapping("/resume")
    public ResponseDto<Void> updateResume(@UserId Long userId, @Valid @ValidFile MultipartFile resume) throws IOException {
        authService.updateResume(userId, resume);
        return ResponseDto.ok(null);
    }

    @PutMapping("/portfolio")
    public ResponseDto<Void> updatePortfolio(@UserId Long userId, @Valid @ValidFile MultipartFile portfolio) throws IOException {
        authService.updatePortfolio(userId, portfolio);
        return ResponseDto.ok(null);
    }
}
