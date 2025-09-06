package com.goormthon.samsamejo.security.factory;

import com.goormthon.samsamejo.domain.type.EProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static Oauth2UserInfo getOAuth2UserInfo(EProvider provider, Map<String, Object> attributes) {
        return switch (provider) {
            case GOOGLE -> new GoogleUserInfo(attributes);
            case KAKAO -> new KakaoUserInfo(attributes);
            case NAVER -> new NaverUserInfo(attributes);
        };
    }
}
