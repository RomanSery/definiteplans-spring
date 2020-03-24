package com.coderdreams.definiteplans.service;


import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import com.coderdreams.definiteplans.oauth.DpOAuth2UserService;
import com.coderdreams.definiteplans.oauth.DpOidcUserService;

@Service
public class SocialService {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final DpOAuth2UserService dpOAuth2UserService;
    private final DpOidcUserService dpOidcUserService;

    public SocialService(ClientRegistrationRepository clientRegistrationRepository, DpOAuth2UserService dpOAuth2UserService, DpOidcUserService dpOidcUserService) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.dpOAuth2UserService = dpOAuth2UserService;
        this.dpOidcUserService = dpOidcUserService;
    }

    public void disconnectSocialAccount(String regId) {
        ClientRegistration clientReg = clientRegistrationRepository.findByRegistrationId(regId);
        dpOAuth2UserService.disconnectAccount(clientReg);
        dpOidcUserService.disconnectAccount(clientReg);
    }
}
