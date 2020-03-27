package com.definiteplans.controller;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.UserStatus;
import com.definiteplans.email.LetterManager;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@Controller
public class RegisterController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ZipCodeRepository zipCodeRepository;
    private final EnumValueRepository enumValueRepository;
    private final LetterManager letterManager;

    public RegisterController(UserRepository userRepository, UserService userService, ZipCodeRepository zipCodeRepository, EnumValueRepository enumValueRepository, LetterManager letterManager) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.zipCodeRepository = zipCodeRepository;
        this.enumValueRepository = enumValueRepository;
        this.letterManager = letterManager;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView m = new ModelAndView("register");
        m.addObject("title", "Register");
        m.addObject("user", new User());
        m.addObject("genders", enumValueRepository.findByType(EnumValueType.GENDER.getId()));
        return m;
    }

    private static class RegValidator {
        private static void validate(User obj, Errors e, UserService userService, UserRepository userRepository, ZipCodeRepository zipCodeRepository) {
            ValidationUtils.rejectIfEmpty(e, "displayName", "", "First Name is required");
            ValidationUtils.rejectIfEmpty(e, "dob", "", "Date of Birth is required");
            ValidationUtils.rejectIfEmpty(e, "gender", "", "Gender is required");
            ValidationUtils.rejectIfEmpty(e, "postalCode", "", "Postal Code is required");
            ValidationUtils.rejectIfEmpty(e, "email", "", "Email is required");
            ValidationUtils.rejectIfEmpty(e, "password", "", "Password is required");

            User u = userRepository.findByEmail(obj.getEmail());
            if (u != null) {
                e.reject("alreadyRegistered", "This email is already being used on our site.  If this is your email you can login or retrieve your password.");
            }
            if (obj.getDob() == null || !DateUtil.isEligible(obj.getDob())) {
                e.reject("validDOB", "Please enter a valid date of birth.");
            }
            if (zipCodeRepository.findById(obj.getPostalCode()).isEmpty()) {
                e.reject("validZip", "Please enter a valid zip code.");
                return;
            }
        }
    }

    @PostMapping("/register")
    public ModelAndView doRegister(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        RegValidator.validate(user, bindingResult, userService, userRepository, zipCodeRepository);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register", Map.of("user", user));
        }

        Optional<ZipCode> zip = zipCodeRepository.findById(user.getPostalCode());

        user.setUserStatus(UserStatus.PENDING_EMAIL_VALIDATION.getId());
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

        letterManager.sendEmailValidationLetter(user);

        return new ModelAndView("reg_thanks", Map.of("email", user.getEmail()));
    }

}
