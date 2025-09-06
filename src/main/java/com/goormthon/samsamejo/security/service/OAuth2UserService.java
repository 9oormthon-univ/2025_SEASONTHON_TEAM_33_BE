package com.goormthon.samsamejo.security.service;

import com.goormthon.samsamejo.domain.Users;
import com.goormthon.samsamejo.domain.type.EProvider;
import com.goormthon.samsamejo.repository.UsersRepository;
import com.goormthon.samsamejo.security.factory.OAuth2UserInfoFactory;
import com.goormthon.samsamejo.security.factory.Oauth2UserInfo;
import com.goormthon.samsamejo.security.info.UserPrincipal;
import com.goormthon.samsamejo.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;
    private final AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("oAuth2UserService called");
        final String registrationId = userRequest.getClientRegistration().getRegistrationId();
        final OAuth2User oAuth2User = super.loadUser(userRequest);

        final EProvider provider = EProvider.valueOf(registrationId.toUpperCase());
        final Oauth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());

        Users user = usersRepository.findByOauthIdAndOauthProvider(oAuth2UserInfo.getId(), provider)
                .orElseGet(() -> authService.saveOAuthUser(oAuth2UserInfo.getId(), provider));

        return UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
    }
}
