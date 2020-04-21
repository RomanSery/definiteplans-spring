package com.definiteplans.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.controller.model.SearchResult;
import com.definiteplans.dao.UserImageRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.User;
import com.definiteplans.service.EnumValueService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;
import com.definiteplans.util.Utils;

@Controller
@RequestMapping(path="/me")
public class MyProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ZipCodeRepository zipCodeRepository;
    private final EnumValueService enumValueService;
    private final UserImageRepository userImageRepository;

    public MyProfileController(UserService userService, UserRepository userRepository, ZipCodeRepository zipCodeRepository, EnumValueService enumValueService, UserImageRepository userImageRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.zipCodeRepository = zipCodeRepository;
        this.enumValueService = enumValueService;
        this.userImageRepository = userImageRepository;
    }

    @GetMapping("/profile")
    public ModelAndView editMyProfile() {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView m = new ModelAndView("my_profile");
        Utils.addEnumValues(m, enumValueService, currUser);
        m.addObject("selectedLanguages", currUser.getLanguageIds());
        m.addObject("user_pics", userImageRepository.findByUserId(currUser.getId()));
        return m;
    }

    @RequestMapping("/refresh-user-pics")
    public String refreshMyPics(Model m) {
        User currUser = userService.getCurrentUser();
        m.addAttribute("user_pics", userImageRepository.findByUserId(currUser.getId()));
        return "edit_profile/imgs :: user-pics-list";
    }


    @PostMapping("/basic")
    public @ResponseBody AjaxResponse saveBasicInfo(@ModelAttribute("user") @Valid User update, BindingResult bindingResult) {
        ProfileValidator.validateBasicInfo(update, bindingResult, userRepository, zipCodeRepository);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            currUser.setDisplayName(update.getDisplayName());
            currUser.setGender(update.getGender());
            currUser.setDob(update.getDob());
            currUser.setCity(update.getCity());
            currUser.setState(update.getState());
            currUser.setPostalCode(update.getPostalCode());
            currUser.setNeighborhood(update.getNeighborhood());
            userService.saveUser(currUser);
        }

        return AjaxResponse.success("Saved");
    }

    @PostMapping("/details")
    public @ResponseBody AjaxResponse saveDetails(@ModelAttribute("user") @Valid User update, BindingResult bindingResult) {

        User currUser = userService.getCurrentUser();
        if(currUser != null) {
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
        }

        return AjaxResponse.success("Saved");
    }


    @PostMapping("/settings")
    public @ResponseBody AjaxResponse saveSettings(@ModelAttribute("user") @Valid User update, BindingResult bindingResult) {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            currUser.setSendNotifications(update.isSendNotifications());
            currUser.setNotificationsEmail(update.getNotificationsEmail());
            currUser.setAgeMax(update.getAgeMax());
            currUser.setAgeMin(update.getAgeMin());
            userService.saveUser(currUser);
        }
        return AjaxResponse.success("Saved");
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
