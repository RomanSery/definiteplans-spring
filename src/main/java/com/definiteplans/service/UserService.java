package com.definiteplans.service;


import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.State;
import com.definiteplans.dom.enumerations.UserStatus;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ZipCodeService zipCodeService;
    private final EnumValueService enumValueService;
    private final ZipCodeRepository zipCodeRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ZipCodeService zipCodeService,
                       EnumValueService enumValueService, ZipCodeRepository zipCodeRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.zipCodeService = zipCodeService;
        this.enumValueService = enumValueService;
        this.zipCodeRepository = zipCodeRepository;
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

    public User createUser(User user) {

        Optional<ZipCode> zip = zipCodeRepository.findById(user.getPostalCode());

        user.setUserStatus(UserStatus.PENDING_EMAIL_VALIDATION.getId());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if(zip.isPresent()) {
            user.setCity(zip.get().getPrimaryCity());
            user.setState(zip.get().getState());
        }
        user.setSendNotifications(true);
        user.setNotificationsEmail(user.getEmail());
        user.setCreationDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());
        user.setUserStatus(UserStatus.ACTIVE.getId());
        user = userRepository.save(user);

        //letterManager.sendEmailValidationLetter(user);
        return user;
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
        }
        return 0;
    }

    public String getCurrUserProfileImg() {
        return getProfileImg(getCurrentUser(), true);
    }

    public String getProfileImg(User u, boolean thumb) {
        if (u != null && u.hasProfilePic()) {
            return thumb ? u.getThumbImgUrl() : u.getFullImgUrl();
        }
        if (u != null && u.getGender() > 0) {
            EnumValue ev = this.enumValueService.getByTypeAndName(EnumValueType.GENDER, "Male");
            if (u.getGender() == ev.getId()) {
                return "/img/male.jpg";
            }
            return "/img/female.jpg";
        }
        return "/img/question_mark.jpg";
    }

    public String getAddrDesc(User u) {
        String s = (u.getState() != null) ? State.valueOfAbbreviation(u.getState()).toString() : "";
        String c = (u.getCity() != null) ? u.getCity() : "";
        String n = (u.getNeighborhood() != null) ? u.getNeighborhood() : "";


        String addr = n + "<br>";
        if (c.length() > 0 && s.length() > 0) {
            addr = addr + c + ", " + s + "<br>";
        } else if (c.length() > 0) {
            addr = addr + c + "<br>";
        } else if (s.length() > 0) {
            addr = addr + s + "<br>";
        }
        return addr;
    }

}
