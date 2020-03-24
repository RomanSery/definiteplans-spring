package com.coderdreams.definiteplans.oauth.util;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class DpOAuth2User extends DefaultOAuth2User implements IDpUser {
    private final Integer userId;

    public DpOAuth2User(OAuth2User oAuth2User, Integer userId, String nameAttributeKey) {
        super(oAuth2User.getAuthorities(), oAuth2User.getAttributes(), nameAttributeKey);
        this.userId = userId;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }
}
