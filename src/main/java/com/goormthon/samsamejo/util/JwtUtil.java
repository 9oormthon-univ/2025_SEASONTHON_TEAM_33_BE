package com.goormthon.samsamejo.util;

import com.goormthon.samsamejo.constant.Constant;
import com.goormthon.samsamejo.domain.type.ERole;
import com.goormthon.samsamejo.dto.response.security.JwtDto;
import com.goormthon.samsamejo.security.info.UserClaims;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil implements InitializingBean {

    @Value("${jwt.access-token-expiration-time}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.refresh-token-expiration-time}")
    private Long refreshTokenExpirationTime;

    @Value("${jwt.secret-key}")
    private String secretKey;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtDto generateTokens(Long id, ERole eRole) {
        return new JwtDto(
                generateToken(id.toString(), eRole, accessTokenExpirationTime),
                accessTokenExpirationTime,
                generateToken(id.toString(), eRole, refreshTokenExpirationTime),
                refreshTokenExpirationTime
        );
    }

    public String generateToken(String id, ERole role, Long expirationTime) {
        Claims claims = Jwts.claims();

        claims.put(Constant.USER_ID_CLAIM_NAME, id);
        claims.put(Constant.USER_ROLE_CLAIM_NAME, role.toString());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Todo: parser 이용 시 예외 처리하는 검증 로직 추가
    public UserClaims getUserClaimsFromToken(String token) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        String userId = claims.get(Constant.USER_ID_CLAIM_NAME, String.class);
        String role = claims.get(Constant.USER_ROLE_CLAIM_NAME, String.class);
        return new UserClaims(Long.valueOf(userId), ERole.valueOf(role));
    }
}
