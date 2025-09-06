package com.goormthon.samsamejo.security.factory;

import java.util.Map;

public class GoogleUserInfo extends Oauth2UserInfo {

    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("sub").toString();
    }
}
