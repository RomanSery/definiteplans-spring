package com.coderdreams.definiteplans.service;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.coderdreams.definiteplans.controller.model.PwdUpdate;
import com.coderdreams.definiteplans.dao.UserEmailRepository;
import com.coderdreams.definiteplans.dao.UserRepository;
import com.coderdreams.definiteplans.dom.User;
import com.coderdreams.definiteplans.dom.UserEmail;
import com.coderdreams.definiteplans.oauth.util.IDpUser;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, UserEmailRepository userEmailRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userEmailRepository = userEmailRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Optional<User> findByFbId(String fbId) {
        return userRepository.findByFbId(fbId);
    }

    public boolean changeUserPassword(final User user, final PwdUpdate pwdUpdate) {
        if(!isCorrectPwd(pwdUpdate.getCurrPwd(), user)) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(pwdUpdate.getPassword1()));
        userRepository.save(user);
        return true;
    }

    public boolean isCorrectPwd(String pwd, User user) {
        return user != null && bCryptPasswordEncoder.matches(pwd, user.getPassword());
    }

    public User createUser(String email, String name, String socialLoginId) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            return user.get();
        }

        Optional<UserEmail> findEmail = userEmailRepository.findByEmail(email);
        if(findEmail.isPresent()) {
            Optional<User> findUser = userRepository.findById(findEmail.get().getUserId());
            if(findUser.isPresent()) {
                return findUser.get();
            }
        }

        if(isLoggedIn()) {
            userEmailRepository.save(new UserEmail(getCurrentUserId(), email));
            return getCurrentUser();
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setDisplayName(name);
        newUser.setCreationDate(LocalDateTime.now());
        newUser.setLastModifiedDate(LocalDateTime.now());
        newUser = userRepository.save(newUser);

        userEmailRepository.save(new UserEmail(newUser.getId(), email));

        return newUser;
    }


    public void updateUser(User user) {
        User userToSave = userRepository.findById(user.getId()).get();
        userToSave.setLastModifiedDate(LocalDateTime.now());
        userToSave.setDisplayName(user.getDisplayName());
        userRepository.save(userToSave);
    }

    public User getUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getCurrentUser() {
        int currUserId = getCurrentUserId();
        if(currUserId == 0) {
            return null;
        }
        return userRepository.findById(currUserId).orElse(null);
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return 0;
        }
        Object principal = authentication.getPrincipal();
        if(principal instanceof org.springframework.security.core.userdetails.User) {
            return Integer.valueOf(authentication.getName());
        } else if(principal instanceof IDpUser) {
            return ((IDpUser) principal).getUserId();
        }
        return 0;
    }
}
