package com.goormthon.samsamejo.security.filter;

import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("exceptionFilter called");
            filterChain.doFilter(request, response);
        } catch (SecurityException e) {
            log.error("SecurityException raised: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.ACCESS_DENIED);
            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {
            log.error("RestExMalformedJwtExceptionception raised: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.MALFORMED_TOKEN);
            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException raised: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_TYPE_ERROR);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException raised: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_EXPIRED);
            filterChain.doFilter(request, response);
        } catch (UnsupportedJwtException e) {
            log.error("UnsupportedJwtException raised: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.TOKEN_UNSUPPORTED);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("JwtException raised: {}", e.getMessage());
            request.setAttribute("exception", ErrorCode.UNKNOWN_TOKEN);
            filterChain.doFilter(request, response);
        } catch (RestException e) {
            log.error("RestException raised: {}", e.getErrorCode().getMessage());
            request.setAttribute("exception", ErrorCode.INTERNAL_SERVER_ERROR);
            filterChain.doFilter(request, response);
        }
    }
}
