package com.definiteplans.oauth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.definiteplans.dom.User;
import com.definiteplans.oauth.provider.GoogleLogin;
import com.definiteplans.oauth.provider.ISocialProvider;
import com.definiteplans.oauth.util.DpOidcUser;
import com.definiteplans.service.UserService;

public class DpOidcUserService extends OidcUserService {

    @Autowired
    private UserService userService;
    @Autowired
    private GoogleLogin googleLogin;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = getEmail(oidcUser, userRequest);
        String displayName = getDisplayName(oidcUser, userRequest);

        User user = userService.createUser(email, displayName, oidcUser.getName());

        createSocialUser(oidcUser, userRequest, user);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        return new DpOidcUser(oidcUser, user.getId(), userNameAttributeName);
    }

    public void disconnectAccount(ClientRegistration clientReg) {
        ISocialProvider provider = getProvider(clientReg);
        if(provider != null) {
            User currUser = userService.getCurrentUser();
            if(currUser != null) {
                provider.disconnectSocialAccount(currUser);
            }
        }
    }

    private String getEmail(OidcUser oidcUser, OidcUserRequest userRequest) {
        ISocialProvider provider = getProvider(userRequest.getClientRegistration());
        if(provider != null) {
            return provider.getEmail(oidcUser, userRequest);
        }
        throw new OAuth2AuthenticationException(new OAuth2Error("666", "provider not found for getEmail", ""));
    }

    private String getDisplayName(OidcUser oidcUser, OidcUserRequest userRequest) {
        ISocialProvider provider = getProvider(userRequest.getClientRegistration());
        if(provider != null) {
            return provider.getDisplayName(oidcUser, userRequest);
        }
        throw new OAuth2AuthenticationException(new OAuth2Error("666", "provider not found for getDisplayName", ""));
    }

    private void createSocialUser(OidcUser oidcUser, OidcUserRequest userRequest, User user) {
        ISocialProvider provider = getProvider(userRequest.getClientRegistration());
        if(provider != null) {
            provider.createSocialUser(oidcUser, userRequest, user);
        }
    }

    private ISocialProvider getProvider(ClientRegistration clientRegistration) {
        if(googleLogin.appliesTo(clientRegistration)) {
            return googleLogin;
        }
        return null;
    }
}
