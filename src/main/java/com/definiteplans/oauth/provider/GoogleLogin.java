package com.definiteplans.oauth.provider;


import java.util.Optional;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.definiteplans.dao.GoogleUserRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.GoogleUser;
import com.definiteplans.dom.User;

@Service
public class GoogleLogin implements ISocialProvider {

    private final GoogleUserRepository googleUserRepository;
    private final UserRepository userRepository;

    public GoogleLogin(GoogleUserRepository googleUserRepository, UserRepository userRepository) {
        this.googleUserRepository = googleUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean appliesTo(ClientRegistration clientRegistration) {
        String regId = clientRegistration.getRegistrationId();
        return "google".equals(regId);
    }

    @Override
    public String getEmail(OidcUser oidcUser, OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(oidcUser, "oidcUser cannot be null");
        return oidcUser.getAttribute("email");
    }

    @Override
    public String getDisplayName(OidcUser oidcUser, OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(oidcUser, "oidcUser cannot be null");
        return oidcUser.getAttribute("name");
    }

    @Override
    public void createSocialUser(OidcUser oidcUser, OidcUserRequest userRequest, User user) {
        Optional<GoogleUser> found = googleUserRepository.findById(user.getId());

        GoogleUser googleUser = found.orElse(new GoogleUser());
        googleUser.setUserId(user.getId());
        googleUser.setSubId(oidcUser.getName());
        googleUser.setAccessToken(userRequest.getAccessToken().getTokenValue());
        googleUser.setIdToken(oidcUser.getIdToken().getTokenValue());
        googleUser.setImgUrl(oidcUser.getPicture());
        googleUserRepository.save(googleUser);

        user.setGoogleSubId(oidcUser.getName());
        userRepository.save(user);
    }

    @Override
    public void disconnectSocialAccount(User user) {
        user.setGoogleSubId(null);
        userRepository.save(user);

        Optional<GoogleUser> found = googleUserRepository.findById(user.getId());
        if(found.isPresent()) {
            googleUserRepository.delete(found.get());
        }
    }

}
