package com.definiteplans.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.definiteplans.dom.enumerations.LoginErrorType;
import com.definiteplans.service.LoginException;

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException {

        if(exception.getCause() instanceof LoginException) {
            LoginException ex = (LoginException) exception.getCause();
            setDefaultFailureUrl("/login?loginerror="+ex.getType().getId()+"&email=" + ex.getUsername());
        } else {
            setDefaultFailureUrl("/login?loginerror="+ LoginErrorType.NOT_FOUND.getId());
        }


        super.onAuthenticationFailure(request, response, exception);

        if(exception.getCause() instanceof LoginException) {
            request.getSession()
                    .setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception.getMessage());
        }
    }
}