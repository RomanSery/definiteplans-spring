package com.definiteplans.config;


import java.util.Arrays;
import java.util.UUID;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import com.definiteplans.oauth.DpOAuth2UserService;
import com.definiteplans.oauth.DpOidcUserService;
import com.definiteplans.oauth.util.OAuth2AccessTokenResponseConverterWithDefaults;
import com.definiteplans.service.DpUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DpUserDetailsService dpUserDetailsService;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final DataSource dataSource;

    public SecurityConfiguration(DpUserDetailsService dpUserDetailsService, AuthenticationFailureHandler authenticationFailureHandler, AuthenticationSuccessHandler authenticationSuccessHandler, DataSource dataSource) {
        this.dpUserDetailsService = dpUserDetailsService;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.dataSource = dataSource;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DpOAuth2UserService dpOAuth2UserService() {
        return new DpOAuth2UserService();
    }

    @Bean
    public DpOidcUserService dpOidcUserService() {
        return new DpOidcUserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(dpUserDetailsService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public RememberMeServices rememberMeServices(String key) {
        MyPersistentTokenBasedRememberMeServices rememberMeServices =
                new MyPersistentTokenBasedRememberMeServices(key, dpUserDetailsService, persistentTokenRepository());
        rememberMeServices.setTokenValiditySeconds(86400);
        rememberMeServices.setParameter("remember");
        rememberMeServices.setCookieName("my-remember-me");

        return rememberMeServices;
    }


    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> authorizationCodeTokenResponseClient() {
        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(new OAuth2AccessTokenResponseConverterWithDefaults());

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        DefaultAuthorizationCodeTokenResponseClient tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
        tokenResponseClient.setRestOperations(restTemplate);

        return tokenResponseClient;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.requiresChannel().anyRequest().requiresSecure();

        // These pages do not require login
        http.authorizeRequests().antMatchers("/login", "/logout", "/forgotpwd", "/health", "/privacy_policy", "/terms", "/news",
                "/forgotpwdthanks", "/register", "/resetpwd", "/unsub", "/confirmemail", "/resendValidationEmail", "/webjars/**", "/fonts/**").permitAll();

        http.authorizeRequests().anyRequest().authenticated();



        // Config for Login Form
        http.authorizeRequests().and().formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error=true")
                .failureHandler(authenticationFailureHandler)
                .successHandler(authenticationSuccessHandler)
                .usernameParameter("loginemail").passwordParameter("loginpassword")
                // Config for Logout Page
                .and().logout().invalidateHttpSession(true).clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout");

        String key = UUID.randomUUID().toString();

        http.rememberMe(new RememberMeConfigurerCustomizer(key));
        http.rememberMe().
                tokenRepository(persistentTokenRepository()).
                userDetailsService(dpUserDetailsService).
                rememberMeServices(rememberMeServices(key));

        http.oauth2Client();
        http.oauth2Login().userInfoEndpoint()
                .userService(dpOAuth2UserService())
                .oidcUserService(dpOidcUserService())
                .and()
                .loginPage("/login/oauth2")
                .tokenEndpoint().accessTokenResponseClient(authorizationCodeTokenResponseClient());
    }



    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/img/**", "/fonts/**", "/webjars/**",
                        "/static/img/manifest.json", "/service-worker.js");
    }

}
