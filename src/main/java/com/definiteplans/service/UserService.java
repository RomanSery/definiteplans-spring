package com.definiteplans.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dao.BlockedUserRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.UserTokenRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.BlockedUser;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.UserToken;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.State;
import com.definiteplans.dom.enumerations.UserStatus;
import com.definiteplans.email.EmailService;
import com.definiteplans.util.DateUtil;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EnumValueService enumValueService;
    private final ZipCodeRepository zipCodeRepository;
    private final BlockedUserRepository blockedUserRepository;
    private final EmailService emailService;
    private final UserTokenRepository userTokenRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       EnumValueService enumValueService, ZipCodeRepository zipCodeRepository, BlockedUserRepository blockedUserRepository, EmailService emailService, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.enumValueService = enumValueService;
        this.zipCodeRepository = zipCodeRepository;
        this.blockedUserRepository = blockedUserRepository;
        this.emailService = emailService;
        this.userTokenRepository = userTokenRepository;
    }

    public boolean changeUserPassword(final User user, final PwdUpdate pwdUpdate) {
        if(!isCorrectPwd(pwdUpdate.getCurrPwd(), user)) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(pwdUpdate.getPassword1()));
        saveUser(user);
        return true;
    }

    public boolean resetForgottenPwd(final User user, final PwdUpdate pwdUpdate) {
        user.setPassword(bCryptPasswordEncoder.encode(pwdUpdate.getPassword1()));
        saveUser(user);
        return true;
    }

    public boolean isCorrectPwd(String pwd, User user) {
        return user != null && bCryptPasswordEncoder.matches(pwd, user.getPassword());
    }

    public User createUser(User user) {

        Optional<ZipCode> zip = zipCodeRepository.findById(user.getPostalCode());
        if(zip.isPresent()) {
            user.setCity(zip.get().getPrimaryCity());
            user.setState(zip.get().getState());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setSendNotifications(true);
        user.setNotificationsEmail(user.getEmail());
        user.setCreationDate(LocalDateTime.now());
        user.setLastModifiedDate(LocalDateTime.now());
        user.setUserStatus(UserStatus.PENDING_EMAIL_VALIDATION.getId());
        user = saveUser(user);

        emailService.sendEmailValidationEmail(user);
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
        String s = (u.getState() != null) ? Objects.requireNonNull(State.valueOfAbbreviation(u.getState())).toString() : "";
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


    public boolean canViewProfile(User currUser, User profile) {
        boolean isViewingSelf = currUser.getId() == profile.getId();
        if(isViewingSelf) {
            return true;
        }

        List<BlockedUser> arr = blockedUserRepository.findByUserId(profile.getId());
        for (BlockedUser bu : arr) {
            if (bu.getBlockedUserId() == currUser.getId()) {
                return false;
            }
        }
        arr = blockedUserRepository.findByUserId(currUser.getId());
        for (BlockedUser bu : arr) {
            if (bu.getBlockedUserId() == profile.getId()) {
                return false;
            }
        }

        int myAge = DateUtil.getAge(currUser.getDob());
        if (myAge > profile.getAgeMax() || myAge < profile.getAgeMin()) {
            return false;
        }
        int profileAge = DateUtil.getAge(profile.getDob());
        if (profileAge > currUser.getAgeMax() || profileAge < currUser.getAgeMin()) {
            return false;
        }

        return true;
    }

    public Integer getUserIdFromToken(Integer id, Integer uid, String tokenStr) {
        if(id == null || uid == null || StringUtils.isBlank(tokenStr)) {
            return null;
        }

        Optional<UserToken> found = userTokenRepository.findById(id);
        if(found.isEmpty()) {
            return null;
        }

        UserToken token = found.get();
        if(!Objects.equals(token.getUserId(), uid) || !token.getToken().equalsIgnoreCase(tokenStr)) {
            return null;
        }

        if(token.getExpiration() != null && token.getExpiration().isBefore(LocalDateTime.now())) {
            return null;
        }

        return token.getUserId();
    }

    public User saveUser(User u) {
        u.setComplete(u.isCompleteProfile());
        u.setLastModifiedDate(LocalDateTime.now());
        u = userRepository.save(u);
        return u;
    }
}
