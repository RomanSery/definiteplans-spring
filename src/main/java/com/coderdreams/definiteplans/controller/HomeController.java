package com.coderdreams.definiteplans.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.coderdreams.definiteplans.dom.User;
import com.coderdreams.definiteplans.service.UserService;

@Controller
public class HomeController {
    private final UserService userService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public HomeController(UserService userService, ClientRegistrationRepository clientRegistrationRepository) {
        this.userService = userService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/")
    public ModelAndView homePage(){
        User currUser = userService.getCurrentUser();

        ModelAndView m = new ModelAndView("home");
        m.addObject("fbLoginUrl", OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/facebook");
        m.addObject("googleLoginUrl", OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/google");

        m.addObject("isFbUser", currUser != null && !StringUtils.isBlank(currUser.getFbId()));
        m.addObject("isGoogleUser", currUser != null && !StringUtils.isBlank(currUser.getGoogleSubId()));

        ClientRegistration facebookReg = clientRegistrationRepository.findByRegistrationId("facebook");
        m.addObject("fbAppId", facebookReg.getClientId());
        return m;
    }
}
