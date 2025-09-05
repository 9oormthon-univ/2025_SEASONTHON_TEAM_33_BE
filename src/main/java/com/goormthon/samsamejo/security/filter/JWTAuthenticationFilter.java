package com.goormthon.samsamejo.security.filter;

import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.security.info.UserClaims;
import com.goormthon.samsamejo.util.HttpHeaderUtil;
import com.goormthon.samsamejo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static com.goormthon.samsamejo.constant.Constant.*;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("jWTAuthenticationFilter called");
        String token = HttpHeaderUtil.getHeaderValue(request, AUTHORIZATION_HEADER, BEARER_PREFIX)
                .orElseThrow(() -> new RestException(ErrorCode.TOKEN_INVALID));

        UserClaims userClaims = jwtUtil.getUserClaimsFromToken(token);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userClaims.id(),
                null,
                Collections.singletonList(new SimpleGrantedAuthority(userClaims.role().getRole()))
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        log.info("shouldNotFilter URI:{}, result={}", request.getRequestURI(), PERMIT_ALL_URLS.contains(request.getRequestURI()));
        return !ADMIN_ONLY_URLS.contains(request.getRequestURI())
                && !USER_AUTH_URLS.contains(request.getRequestURI())
                && PERMIT_ALL_URLS.contains(request.getRequestURI());
    }
}
