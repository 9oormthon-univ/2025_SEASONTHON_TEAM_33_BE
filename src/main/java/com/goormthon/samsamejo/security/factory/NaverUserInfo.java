package com.goormthon.samsamejo.security.factory;

import java.util.Map;

public class NaverUserInfo extends Oauth2UserInfo {

    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response == null)
            return null;
        return response.get("id").toString();
    }
}
