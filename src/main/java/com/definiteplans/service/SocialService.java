package com.definiteplans.service;


import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import com.definiteplans.oauth.CwfOAuth2UserService;
import com.definiteplans.oauth.CwfOidcUserService;


@Service
public class SocialService {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CwfOAuth2UserService cwfOAuth2UserService;
    private final CwfOidcUserService cwfOidcUserService;

    public SocialService(ClientRegistrationRepository clientRegistrationRepository, CwfOAuth2UserService cwfOAuth2UserService, CwfOidcUserService cwfOidcUserService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.cwfOAuth2UserService = cwfOAuth2UserService;
        this.cwfOidcUserService = cwfOidcUserService;
    }

    public void disconnectSocialAccount(String regId) {
        ClientRegistration clientReg = clientRegistrationRepository.findByRegistrationId(regId);
        cwfOAuth2UserService.disconnectAccount(clientReg);
        cwfOidcUserService.disconnectAccount(clientReg);
    }
}
