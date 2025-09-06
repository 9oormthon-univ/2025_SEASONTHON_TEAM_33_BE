package com.goormthon.samsamejo.security.factory;

import java.util.Map;

public class KakaoUserInfo extends Oauth2UserInfo {

    public KakaoUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }
}
