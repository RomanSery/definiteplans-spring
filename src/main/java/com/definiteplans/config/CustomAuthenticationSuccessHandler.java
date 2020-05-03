package com.definiteplans.config;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.User;
import com.definiteplans.util.DateUtil;

@Component("authenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;

    public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if(principal instanceof org.springframework.security.core.userdetails.User) {
            Integer userId = Integer.valueOf(authentication.getName());
            Optional<User> found = userRepository.findById(userId);
            if(found.isPresent()) {
                User u = found.get();
                u.setLastLoginDate(DateUtil.now());
                userRepository.save(u);
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}