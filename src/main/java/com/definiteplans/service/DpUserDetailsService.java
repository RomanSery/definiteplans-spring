package com.definiteplans.service;


import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.LoginErrorType;
import com.definiteplans.dom.enumerations.UserStatus;

@Service
public class DpUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DpUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getUser(String username) {

        if(NumberUtils.isCreatable(username)) {
            int userId = NumberUtils.toInt(username);
            if(userId > 0) {
                Optional<User> found = userRepository.findById(userId);
                return found.isPresent() ? found.get() : null;
            }

            //must be a social ID
            User user = userRepository.findByGoogleSubId(username);
            if(user != null) {
                return user;
            }
            user = userRepository.findByFbId(username);
            if(user != null) {
                return user;
            }
        }
        return userRepository.findByEmail(username);
    }

    private boolean isSocialLogin(String username) {
        if(NumberUtils.isCreatable(username)) {
            int userId = NumberUtils.toInt(username);
            if(userId == 0) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        if (user == null) {
            throw new LoginException(LoginErrorType.NOT_FOUND, username);
        }
        if (user.getUserStatus() == UserStatus.DISABLED.getId()) {
            throw new LoginException(LoginErrorType.DISABLED, username);
        }
        if(user.getUserStatus() != UserStatus.ACTIVE.getId()) {
            throw new LoginException(LoginErrorType.PENDING, username);
        }

        if(StringUtils.isBlank(user.getPassword()) && !isSocialLogin(username)) {
            if(!StringUtils.isBlank(user.getFbId())) {
                throw new LoginException(LoginErrorType.FB_NO_PWD, username);
            }
            if(!StringUtils.isBlank(user.getGoogleSubId())) {
                throw new LoginException(LoginErrorType.GOOGLE_NO_PWD, username);
            }
        }

        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(String.valueOf(user.getId()));
        builder.password(user.getPassword() != null ? user.getPassword() : "");
        builder.roles("USER");
        return builder.build();
    }
}
