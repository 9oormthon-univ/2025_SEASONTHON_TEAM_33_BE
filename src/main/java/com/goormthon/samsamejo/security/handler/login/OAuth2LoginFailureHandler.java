package com.goormthon.samsamejo.security.handler.login;

import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
    ) {
        log.info("oAuth2LoginFailureHandler called");
        throw new RestException(ErrorCode.LOGIN_FAILURE);
    }
}
