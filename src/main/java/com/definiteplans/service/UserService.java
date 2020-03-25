package com.definiteplans.service;


import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dao.UserEmailRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.UserEmail;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.State;
import com.definiteplans.dom.enumerations.UserStatus;
import com.definiteplans.util.DateUtil;
import com.definiteplans.util.Utils;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final Pattern DEFAULT_PASSWORD_VALIDATION_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[a-z]).{6,20})");

    private int validationLinkExpirationHours;

    private final UserRepository userRepository;
    private final UserEmailRepository userEmailRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ZipCodeService zipCodeService;
    private final EnumValueService enumValueService;
    private final CipherService cipherService;
    private final RandomNumberGenerator rng;

    public UserService(UserRepository userRepository, UserEmailRepository userEmailRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder, ZipCodeService zipCodeService, EnumValueService enumValueService, CipherService cipherService, RandomNumberGenerator rng) {
        this.userRepository = userRepository;
        this.userEmailRepository = userEmailRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.zipCodeService = zipCodeService;
        this.enumValueService = enumValueService;
        this.cipherService = cipherService;
        this.rng = rng;
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
        User user = userRepository.findByEmail(email);
        if(user != null) {
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
        newUser.setCreationDate(LocalDateTime.now());
        newUser.setLastModifiedDate(LocalDateTime.now());
        newUser.setUserStatus(UserStatus.ACTIVE.getId());
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
                return "img/male.jpg";
            }
            return "img/female.jpg";
        }
        return "img/question_mark.jpg";
    }

    public List<User> browsePagedResults(User currUser, long first, long count, Integer ageFrom, Integer ageTo, Integer heightFrom, Integer heightTo, List<String> distanceFrom, String state, List<Integer> ethnicity, List<Integer> maritalStatus, List<Integer> kids, List<Integer> wantsKids, List<Integer> languages, List<Integer> religion, List<Integer> education, List<Integer> income, List<Integer> smokes, List<Integer> gender) {
        List<User> list = userRepository.browsePagedResults(currUser, first, count, ageFrom, ageTo, heightFrom, heightTo, distanceFrom, state, ethnicity, maritalStatus, kids, wantsKids, languages, religion, education, income, smokes, gender);
        return (list == null) ? new ArrayList<>() : list;
    }

    public List<User> getSearchResults(User currUser) {
        Integer ageFrom = currUser.getSinglePrefIntVal("ageFrom");
        Integer ageTo = currUser.getSinglePrefIntVal("ageTo");
        Integer heightFrom = currUser.getSinglePrefIntVal("heightFrom");
        Integer heightTo = currUser.getSinglePrefIntVal("heightTo");
        String state = (currUser.getSinglePrefState("name") != null) ? currUser.getSinglePrefState("name").getAbbreviation() : null;

        List<Integer> ethnicity = Utils.getFilterValues2(currUser.getMultiPref("ethnicity"));
        List<Integer> maritalStatus = Utils.getFilterValues2(currUser.getMultiPref("maritalStatus"));
        List<Integer> kids = Utils.getFilterValues2(currUser.getMultiPref("kids"));
        List<Integer> wantsKids = Utils.getFilterValues2(currUser.getMultiPref("wantsKids"));
        List<Integer> languages = Utils.getFilterValues2(currUser.getMultiPref("languages"));
        List<Integer> religion = Utils.getFilterValues2(currUser.getMultiPref("religion"));
        List<Integer> education = Utils.getFilterValues2(currUser.getMultiPref("education"));
        List<Integer> income = Utils.getFilterValues2(currUser.getMultiPref("income"));
        List<Integer> smokes = Utils.getFilterValues2(currUser.getMultiPref("smokes"));
        List<Integer> gender = Utils.getFilterValues2(currUser.getMultiPref("gender"));
        Integer distance = currUser.getSinglePrefIntVal("distanceFrom");

        List<String> distanceFrom = new ArrayList<>();
        if (distance != null && distance.intValue() > 0 && currUser.getPostalCode() != null && currUser.getPostalCode().length() > 0) {
            ZipCode zip = this.zipCodeService.getById(currUser.getPostalCode());
            if (zip != null) {
                List<ZipCode> arr = this.zipCodeService.getZipCodesByRadius(zip, distance.intValue());
                for (ZipCode z : arr) distanceFrom.add("'" + z.getZip() + "'");

            }
        }

        return browsePagedResults(currUser, 0L, 0L, ageFrom, ageTo, heightFrom, heightTo, distanceFrom, state, ethnicity, maritalStatus, kids, wantsKids, languages, religion, education, income, smokes, gender);
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

    public boolean updatePassword(User user, String plaintextPassword) {
        if (user == null || plaintextPassword == null || plaintextPassword.length() <= 0) {
            return false;
        }

        user.setHashedPwdAndSaltViaPlaintextPwd(plaintextPassword, this);
        userRepository.save(user);
        return true;
    }


    public String[] getHashedPasswordAndSalt(String plainTextPassword) {
        String saltBase64 = this.rng.nextBytes().toBase64();
        return getHashedPasswordAndSalt(saltBase64, plainTextPassword);
    }

    private String[] getHashedPasswordAndSalt(String salt, String plainTextPassword) {
        String hashedPasswordBase64 = (new Sha256Hash(plainTextPassword, salt, 1024)).toBase64();
        String[] hashedPasswordAndSalt = {hashedPasswordBase64, salt};
        return hashedPasswordAndSalt;
    }

    public boolean isCorrectPassword(User user, String plainTextPassword) {
        String[] hashedPasswordAndSalt = getHashedPasswordAndSalt(user.getSalt(), plainTextPassword);
        return user.getPassword().equals(hashedPasswordAndSalt[0]);
    }

    public String validateEncryptedExpiringUserValidationString(String encryptedExpiringUserValidationString) throws ExpiredUserValidationStringException {
        String decryptedString = null;
        try {
            decryptedString = this.cipherService.base64DecodeAndDecrypt(encryptedExpiringUserValidationString);
        } catch (Exception e) {
            logger.debug("error decrypting str {}", encryptedExpiringUserValidationString, e);
            return null;
        }
        logger.debug("decrypted string: {}", decryptedString);
        String[] tokens = decryptedString.split("\\|");
        if (tokens.length != 2) {
            return null;
        }
        try {
            long expireTime = Long.valueOf(tokens[0]).longValue();
            if (DateUtil.getCurrentServerTime().toInstant(ZoneOffset.UTC).toEpochMilli() > expireTime) {
                throw new ExpiredUserValidationStringException(tokens[1]);
            }
            return tokens[1];
        } catch (NumberFormatException e1) {
            logger.debug("invalid expire time", e1);
            return null;
        }
    }

    public Pattern getPasswordValidationPattern() {
        return DEFAULT_PASSWORD_VALIDATION_PATTERN;
    }


    public String createEncryptedExpiringUserValidationString(User myUser) throws UnsupportedEncodingException {
        return this.cipherService.encryptAndBase64Encode(DateUtil.getCurrentServerTime(validationLinkExpirationHours).toInstant(ZoneOffset.UTC).toEpochMilli() + "|" + myUser.getEmail());
    }

    public int getValidationLinkExpirationHours() {
        return this.validationLinkExpirationHours;
    }


    public void setValidationLinkExpirationHours(int validationLinkExpirationHours) {
        this.validationLinkExpirationHours = validationLinkExpirationHours;
    }

}
