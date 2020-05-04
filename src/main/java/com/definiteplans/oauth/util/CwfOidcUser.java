package com.definiteplans.oauth.util;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;


public class CwfOidcUser extends DefaultOidcUser implements ICwfUser {
    private final Integer userId;

    public CwfOidcUser(OidcUser oidcUser, Integer userId, String nameAttributeKey) {
        super(oidcUser.getAuthorities(), oidcUser.getIdToken(), oidcUser.getUserInfo(), nameAttributeKey);
        this.userId = userId;
    }

    @Override
    public Integer getUserId() {
        return userId;
    }
}
