package com.definiteplans.controller;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dao.UserImageRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;
import com.definiteplans.util.Utils;

@Controller
@RequestMapping(path="/me")
public class MyProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ZipCodeRepository zipCodeRepository;
    private final EnumValueRepository enumValueRepository;
    private final UserImageRepository userImageRepository;

    public MyProfileController(UserService userService, UserRepository userRepository, ZipCodeRepository zipCodeRepository, EnumValueRepository enumValueRepository, UserImageRepository userImageRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.zipCodeRepository = zipCodeRepository;
        this.enumValueRepository = enumValueRepository;
        this.userImageRepository = userImageRepository;
    }

    @GetMapping("/profile")
    public ModelAndView editMyProfile() {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView m = new ModelAndView("my_profile");
        Utils.addEnumValues(m, enumValueRepository, currUser);
        m.addObject("selectedLanguages", currUser.getLanguageIds());
        m.addObject("user_pics", userImageRepository.findByUserId(currUser.getId()));
        return m;
    }

    @PostMapping("/basic")
    public ModelAndView saveBasicInfo(@ModelAttribute("user") @Valid User update, BindingResult bindingResult) {
        ProfileValidator.validateBasicInfo(update, bindingResult, userRepository, zipCodeRepository);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("my_profile", Map.of("user", update));
        }

        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        currUser.setDisplayName(update.getDisplayName());
        currUser.setGender(update.getGender());
        currUser.setDob(update.getDob());
        currUser.setCity(update.getCity());
        currUser.setState(update.getState());
        currUser.setPostalCode(update.getPostalCode());
        currUser.setNeighborhood(update.getNeighborhood());
        userService.saveUser(currUser);
        return new ModelAndView(new RedirectView("/me/profile"));
    }

    @PostMapping("/details")
    public ModelAndView saveDetails(@ModelAttribute("user") User update, BindingResult bindingResult) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        currUser.setLanguages(update.getLanguages());
        currUser.setReligion(update.getReligion());
        currUser.setIncome(update.getIncome());
        currUser.setKids(update.getKids());
        currUser.setWantsKids(update.getWantsKids());
        currUser.setMaritalStatus(update.getMaritalStatus());
        currUser.setEducation(update.getEducation());
        currUser.setSmokes(update.getSmokes());
        currUser.setEthnicity(update.getEthnicity());
        currUser.setAboutMe(update.getAboutMe());
        currUser.setInterests(update.getInterests());
        currUser.setHeight(update.getHeight());

        userService.saveUser(currUser);
        return new ModelAndView(new RedirectView("/me/profile"));
    }

    @PostMapping("/settings")
    public ModelAndView saveSettings(@ModelAttribute("user") @Valid User update, BindingResult bindingResult) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        currUser.setSendNotifications(update.isSendNotifications());
        currUser.setNotificationsEmail(update.getNotificationsEmail());
        currUser.setAgeMax(update.getAgeMax());
        currUser.setAgeMin(update.getAgeMin());

        userService.saveUser(currUser);
        return new ModelAndView(new RedirectView("/me/profile"));
    }



    private static class ProfileValidator {
        private static void validateBasicInfo(User obj, Errors e, UserRepository userRepository, ZipCodeRepository zipCodeRepository) {
            ValidationUtils.rejectIfEmpty(e, "displayName", "", "First Name is required");
            ValidationUtils.rejectIfEmpty(e, "dob", "", "Date of Birth is required");
            ValidationUtils.rejectIfEmpty(e, "gender", "", "Gender is required");
            ValidationUtils.rejectIfEmpty(e, "postalCode", "", "Postal Code is required");

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
}
