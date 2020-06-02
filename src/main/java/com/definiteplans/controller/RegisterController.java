package com.definiteplans.controller;

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

import com.definiteplans.dao.UserEmailRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.User;
import com.definiteplans.dom.UserEmail;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.service.EnumValueService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@Controller
public class RegisterController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ZipCodeRepository zipCodeRepository;
    private final EnumValueService enumValueService;
    private final UserEmailRepository userEmailRepository;

    public RegisterController(UserRepository userRepository, UserService userService, ZipCodeRepository zipCodeRepository, EnumValueService enumValueService, UserEmailRepository userEmailRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.zipCodeRepository = zipCodeRepository;
        this.enumValueService = enumValueService;
        this.userEmailRepository = userEmailRepository;
    }

    @GetMapping("/register")
    public ModelAndView register() {
        ModelAndView m = new ModelAndView("register");
        m.addObject("title", "Register");
        m.addObject("user", new User());
        m.addObject("genders", enumValueService.findByType(EnumValueType.GENDER));
        return m;
    }

    private static class RegValidator {
        private static void validate(User obj, Errors e, UserRepository userRepository, ZipCodeRepository zipCodeRepository, UserEmailRepository userEmailRepository) {
            ValidationUtils.rejectIfEmpty(e, "displayName", "", "Display Name is required");
            ValidationUtils.rejectIfEmpty(e, "dob", "", "Date of Birth is required");
            ValidationUtils.rejectIfEmpty(e, "gender", "", "Gender is required");
            ValidationUtils.rejectIfEmpty(e, "postalCode", "", "Postal Code is required");
            ValidationUtils.rejectIfEmpty(e, "email", "", "Email is required");
            ValidationUtils.rejectIfEmpty(e, "password", "", "Password is required");

            User u = userRepository.findByEmail(obj.getEmail());
            if (u != null) {
                e.reject("alreadyRegistered", "This email is already being used on our site.  If this is your email you can login or retrieve your password.");
            } else {
                Optional<UserEmail> foundEmail = userEmailRepository.findByEmail(obj.getEmail());
                if(foundEmail.isPresent()) {
                    e.reject("alreadyRegistered", "This email is already being used on our site.  If this is your email you can login or retrieve your password.");
                }
            }

            if (obj.getDob() == null || !DateUtil.isEligible(obj.getDob())) {
                e.reject("validDOB", "Please enter a valid date of birth.");
            }
            if (zipCodeRepository.findById(obj.getPostalCode()).isEmpty()) {
                e.reject("validZip", "Please enter a valid zip code.");
                return;
            }

            if(obj.getDisplayName() != null && (obj.getDisplayName().length() < 5 || obj.getDisplayName().length() > 15)) {
                e.reject("displayNameInvalid", "Display name must be 5-15 characters long.");
            }
        }
    }

    @PostMapping("/register")
    public ModelAndView doRegister(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        RegValidator.validate(user, bindingResult, userRepository, zipCodeRepository, userEmailRepository);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("register", Map.of("user", user));
        }

        user = userService.createUser(user);
        return new ModelAndView("reg_thanks", Map.of("email", user.getEmail()));
    }

}
