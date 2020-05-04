package com.definiteplans.oauth.provider;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.definiteplans.dom.User;

public interface ISocialProvider {
    boolean appliesTo(ClientRegistration clientRegistration);

    default String getEmail(OAuth2User oAuth2User, OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }
    default String getDisplayName(OAuth2User oAuth2User, OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }
    default void createSocialUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest, User user) {

    }


    default String getEmail(OidcUser oidcUser, OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }
    default String getDisplayName(OidcUser oidcUser, OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        return null;
    }
    default void createSocialUser(OidcUser oidcUser, OidcUserRequest userRequest, User user) {

    }


    void disconnectSocialAccount(User user);
}
