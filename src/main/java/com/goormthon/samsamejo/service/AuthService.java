package com.goormthon.samsamejo.service;

import com.goormthon.samsamejo.domain.Users;
import com.goormthon.samsamejo.domain.type.EProvider;
import com.goormthon.samsamejo.domain.type.ERole;
import com.goormthon.samsamejo.domain.type.Education;
import com.goormthon.samsamejo.dto.response.security.JwtDto;
import com.goormthon.samsamejo.exception.ErrorCode;
import com.goormthon.samsamejo.exception.RestException;
import com.goormthon.samsamejo.repository.UsersRepository;
import com.goormthon.samsamejo.security.info.UserClaims;
import com.goormthon.samsamejo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.goormthon.samsamejo.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void updateUserTokens(String refreshToken, Long userId) {
        usersRepository.updateRefreshTokenByUserId(refreshToken, userId);
    }

    @Transactional
    public Users saveOAuthUser(String oauthId, EProvider oauthProvider) {
        return usersRepository.save(Users.builder()
                .oauthId(oauthId)
                .role(ERole.USER)
                .oauthProvider(oauthProvider)
                .build()
        );
    }

    @Transactional
    public JwtDto reissue(String refreshToken) {
        UserClaims userClaims = jwtUtil.getUserClaimsFromToken(refreshToken);

        Users user = usersRepository.findById(userClaims.id()).orElseThrow(() -> new RestException(TOKEN_INVALID));
        JwtDto jwtDto = jwtUtil.generateTokens(user.getId(), user.getRole());

        usersRepository.updateRefreshTokenByUserId(jwtDto.refreshToken(), user.getId());
        return jwtDto;
    }

    @Transactional
    public void createUserInfo(
            Long userId,
            String name,
            String email,
            String phoneNumber,
            Education education,
            String major,
            List<String> interestFields,
            List<String> skills) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RestException(NOT_FOUND_USER));
        user.updateUserInfo(name, email, phoneNumber, education, major, interestFields, skills);
        user.register();
    }
}
