package com.definiteplans.config;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;

public class RememberMeConfigurerCustomizer implements Customizer<RememberMeConfigurer<HttpSecurity>> {

    private final String key;

    public RememberMeConfigurerCustomizer(String key) {
        this.key = key;
    }

    @Override
    public void customize(RememberMeConfigurer<HttpSecurity> httpSecurityRememberMeConfigurer) {
        httpSecurityRememberMeConfigurer.key(key);
    }
}
