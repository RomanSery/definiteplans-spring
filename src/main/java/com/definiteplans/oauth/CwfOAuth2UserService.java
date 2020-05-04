package com.definiteplans.oauth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.definiteplans.dom.User;
import com.definiteplans.oauth.provider.FacebookLogin;
import com.definiteplans.oauth.provider.ISocialProvider;
import com.definiteplans.oauth.util.CwfOAuth2User;
import com.definiteplans.service.UserService;


public class CwfOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserService userService;
    @Autowired
    private FacebookLogin fbLogin;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = getEmail(oAuth2User, userRequest);
        String displayName = getDisplayName(oAuth2User, userRequest);

        User user = userService.createUser(email, displayName, oAuth2User.getName());

        createSocialUser(oAuth2User, userRequest, user);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        return new CwfOAuth2User(oAuth2User, user.getId(), userNameAttributeName);
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

    private String getEmail(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        ISocialProvider provider = getProvider(userRequest.getClientRegistration());
        if(provider != null) {
            return provider.getEmail(oAuth2User, userRequest);
        }
        throw new OAuth2AuthenticationException(new OAuth2Error("666", "provider not found for getEmail", ""));
    }

    private String getDisplayName(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        ISocialProvider provider = getProvider(userRequest.getClientRegistration());
        if(provider != null) {
            return provider.getDisplayName(oAuth2User, userRequest);
        }
        throw new OAuth2AuthenticationException(new OAuth2Error("666", "provider not found for getDisplayName", ""));
    }

    private void createSocialUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest, User user) {
        ISocialProvider provider = getProvider(userRequest.getClientRegistration());
        if(provider != null) {
            provider.createSocialUser(oAuth2User, userRequest, user);
        }
    }


    private ISocialProvider getProvider(ClientRegistration clientRegistration) {
        if(fbLogin.appliesTo(clientRegistration)) {
            return fbLogin;
        }
        return null;
    }

}
