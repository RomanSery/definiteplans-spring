package com.coderdreams.definiteplans.oauth.provider;


import java.util.Optional;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.coderdreams.definiteplans.dao.UserRepository;
import com.coderdreams.definiteplans.dao.FbUserRepository;
import com.coderdreams.definiteplans.dom.User;
import com.coderdreams.definiteplans.dom.FbUser;

@Service
public class FacebookLogin implements ISocialProvider {

    private final FbUserRepository fbUserRepository;
    private final UserRepository userRepository;

    public FacebookLogin(FbUserRepository fbUserRepository, UserRepository userRepository) {
        this.fbUserRepository = fbUserRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean appliesTo(ClientRegistration clientRegistration) {
        String regId = clientRegistration.getRegistrationId();
        return "facebook".equals(regId);
    }

    @Override
    public String getEmail(OAuth2User oAuth2User, OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(oAuth2User, "oAuth2User cannot be null");
        return oAuth2User.getAttribute("email");
    }

    @Override
    public String getDisplayName(OAuth2User oAuth2User, OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(oAuth2User, "oAuth2User cannot be null");
        return oAuth2User.getAttribute("name");
    }

    @Override
    public void createSocialUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest, User user) {
        Optional<FbUser> found = fbUserRepository.findById(user.getId());

        FbUser fbUser = found.orElse(new FbUser());
        fbUser.setUserId(user.getId());
        fbUser.setFbId(oAuth2User.getName());
        fbUser.setAccessToken(userRequest.getAccessToken().getTokenValue());
        fbUserRepository.save(fbUser);

        user.setFbId(oAuth2User.getName());
        userRepository.save(user);
    }

    @Override
    public void disconnectSocialAccount(User user) {
        user.setFbId(null);
        userRepository.save(user);

        Optional<FbUser> found = fbUserRepository.findById(user.getId());
        if(found.isPresent()) {
            fbUserRepository.delete(found.get());
        }
    }
}
