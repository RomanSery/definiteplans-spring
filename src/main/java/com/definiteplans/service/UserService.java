package com.definiteplans.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.definiteplans.controller.model.BlockedUserRow;
import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dao.BlockedUserRepository;
import com.definiteplans.dao.UserEmailRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.UserTokenRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.BlockedUser;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.UserEmail;
import com.definiteplans.dom.UserToken;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.State;
import com.definiteplans.dom.enumerations.UserStatus;
import com.definiteplans.email.EmailService;
import com.definiteplans.oauth.util.IDpUser;
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
    private final UserEmailRepository userEmailRepository;
    private final DateService dateService;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       EnumValueService enumValueService, ZipCodeRepository zipCodeRepository, BlockedUserRepository blockedUserRepository,
                       EmailService emailService, UserTokenRepository userTokenRepository, UserEmailRepository userEmailRepository, DateService dateService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.enumValueService = enumValueService;
        this.zipCodeRepository = zipCodeRepository;
        this.blockedUserRepository = blockedUserRepository;
        this.emailService = emailService;
        this.userTokenRepository = userTokenRepository;
        this.userEmailRepository = userEmailRepository;
        this.dateService = dateService;
    }

    public boolean changeUserPassword(final User user, final PwdUpdate pwdUpdate) {
        if(user.hasPwd() && !isCorrectPwd(pwdUpdate.getCurrPwd(), user)) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(pwdUpdate.getPassword1()));
        saveUser(user, true);
        return true;
    }

    public boolean resetForgottenPwd(final User user, final PwdUpdate pwdUpdate) {
        user.setPassword(bCryptPasswordEncoder.encode(pwdUpdate.getPassword1()));
        saveUser(user, true);
        return true;
    }

    public boolean isCorrectPwd(String pwd, User user) {
        return user != null && bCryptPasswordEncoder.matches(pwd, user.getPassword());
    }

    public User createUser(User user) {

        User found = userRepository.findByEmail(user.getEmail());
        if(found != null) {
            return found;
        }

        Optional<UserEmail> findEmail = userEmailRepository.findByEmail(user.getEmail());
        if(findEmail.isPresent()) {
            Optional<User> findUser = userRepository.findById(findEmail.get().getUserId());
            if(findUser.isPresent()) {
                return findUser.get();
            }
        }

        Optional<ZipCode> zip = zipCodeRepository.findById(user.getPostalCode());
        if(zip.isPresent()) {
            user.setCity(zip.get().getPrimaryCity());
            user.setState(zip.get().getState());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setSendNotifications(true);
        user.setCreationDate(DateUtil.now());
        user.setLastModifiedDate(DateUtil.now());
        user.setUserStatus(UserStatus.PENDING_EMAIL_VALIDATION.getId());
        user = saveUser(user, true);

        userEmailRepository.save(new UserEmail(user.getId(), user.getEmail()));

        emailService.sendEmailValidationEmail(user);
        return user;
    }

    public void resendValidationEmail(String email) {
        if(StringUtils.isBlank(email)) {
            return;
        }
        User found = userRepository.findByEmail(email);
        if(found != null) {
            emailService.sendEmailValidationEmail(found);
        }
    }


    public User createUser(String email, String name, String socialLoginId) {
        User user = userRepository.findByEmail(email);
        if(user != null) {
            if(user.getUserStatus() == UserStatus.PENDING_EMAIL_VALIDATION.getId()) {
                user.setUserStatus(UserStatus.ACTIVE.getId());
                saveUser(user, false);
            }
            return user;
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
        newUser.setCreationDate(DateUtil.now());
        newUser.setLastModifiedDate(DateUtil.now());
        newUser.setUserStatus(UserStatus.ACTIVE.getId());
        newUser = userRepository.save(newUser);

        userEmailRepository.save(new UserEmail(newUser.getId(), email));

        return newUser;
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

    public static String getLastOnline(User u) {
        if(isOnlineNow(u)) {
            return "Now";
        }
        return "Last online " + DateUtil.getTimeDifferenceDescription(getMostRecentDateForUser(u));
    }
    public static boolean isOnlineNow(User u) {
        LocalDateTime mostRecent = getMostRecentDateForUser(u);
        return mostRecent != null && DateUtil.getMinutesBetween(mostRecent, DateUtil.now()) <= 30;
    }

    private static LocalDateTime getMostRecentDateForUser(User u) {
        LocalDateTime d1 = u.getLastLoginDate();
        LocalDateTime d2 = u.getLastModifiedDate();
        LocalDateTime mostRecent = d1;

        if(mostRecent == null || (d2 != null && d2.isAfter(d1))) {
            mostRecent = d2;
        }
        return mostRecent;
    }

    public static String getAddrDesc(User u) {
        String s = (u.getState() != null) ? Objects.requireNonNull(State.valueOfAbbreviation(u.getState())).toString() : "";
        String c = (u.getCity() != null) ? u.getCity() : "";
        String n = (u.getNeighborhood() != null) ? u.getNeighborhood() : "";

        List<String> components = new ArrayList<>();
        if(!StringUtils.isBlank(u.getNeighborhood())) {
            components.add(u.getNeighborhood());
        }
        if (c.length() > 0 && s.length() > 0) {
            components.add(c + ", " + s);
        } else if (c.length() > 0) {
            components.add(c);
        } else if (s.length() > 0) {
            components.add(s);
        }

        return StringUtils.join(components, "<br>");
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
        if ( (profile.getAgeMax() > 0 && myAge > profile.getAgeMax()) || (myAge < profile.getAgeMin())) {
            return false;
        }
        int profileAge = DateUtil.getAge(profile.getDob());
        if ( (currUser.getAgeMax() > 0 && profileAge > currUser.getAgeMax()) || profileAge < currUser.getAgeMin()) {
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

        if(token.getExpiration() != null && token.getExpiration().isBefore(DateUtil.now())) {
            return null;
        }

        return token.getUserId();
    }

    public static Integer getProfileAge(User profile) {
        Integer age = (profile.getDob() != null) ? DateUtil.getAge(profile.getDob()) : null;
        if (age != null && age.intValue() < 18) age = Integer.valueOf(18);
        return age;
    }

    public User saveUser(User u, boolean updateLastModified) {
        u.setComplete(u.isCompleteProfile());
        if(updateLastModified) {
            u.setLastModifiedDate(DateUtil.now());
        }
        u = userRepository.save(u);
        return u;
    }


    public boolean blockUser(int userId) {

        int currUserId = getCurrentUserId();
        if(currUserId <= 0 || userId <= 0) {
            return false;
        }

        if(blockedUserRepository.countByUserIdAndBlockedUserId(currUserId, userId) > 0) {
            //already blocked
            return true;
        }

        BlockedUser blocked = new BlockedUser();
        blocked.setUserId(currUserId);
        blocked.setBlockedUserId(userId);
        blocked.setBlockedDate(DateUtil.now());
        blockedUserRepository.save(blocked);

        dateService.onBlockUser(currUserId, userId);
        return true;
    }

    public boolean unBlockUser(int userId) {

        int currUserId = getCurrentUserId();
        if(currUserId <= 0 || userId <= 0) {
            return false;
        }

        BlockedUser blockedUser = blockedUserRepository.findByUserIdAndBlockedUserId(currUserId, userId);
        if(blockedUser != null) {
            blockedUserRepository.delete(blockedUser);
        }

        return true;
    }

    public List<BlockedUserRow> getBlockedUserRows() {
        int currUserId = getCurrentUserId();
        List<BlockedUser> arr = blockedUserRepository.findByUserId(currUserId);
        List<BlockedUserRow> blockedUsers = new ArrayList<>(arr.size());
        for(BlockedUser bu : arr) {
            Optional<User> u = userRepository.findById(bu.getBlockedUserId());
            if(u.isPresent()) {
                blockedUsers.add(new BlockedUserRow(bu.getBlockedUserId(), u.get().getDisplayName()));
            }
        }
        return blockedUsers;
    }


    public String getProfileVal(int n) {
        if(n == 0) {
            return "";
        }
        EnumValue enumValue = enumValueService.findById(n);
        return enumValue != null ? enumValue.getEnumValue() : "";
    }

    public String getLanguages(User profile) {
        List<String> languages = new ArrayList<>();
        if (profile.getLanguages() != null && profile.getLanguages().length() > 0) {
            for (String languageId : profile.getLanguages().split(",")) {
                languages.add(getProfileVal(Integer.parseInt(languageId)));
            }
        }
        return StringUtils.join(languages, ",");
    }


    public void deleteAccount() {
        int currUserId = getCurrentUserId();
        userRepository.deleteAccount(currUserId);
    }
}
