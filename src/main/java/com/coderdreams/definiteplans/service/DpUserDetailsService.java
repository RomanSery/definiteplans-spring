package com.coderdreams.definiteplans.service;


import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coderdreams.definiteplans.dao.UserRepository;
import com.coderdreams.definiteplans.dom.User;

@Service
public class DpUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DpUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found.");
        }


        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(String.valueOf(user.get().getId()));
        builder.password(user.get().getPassword());
        return builder.build();
    }
}
