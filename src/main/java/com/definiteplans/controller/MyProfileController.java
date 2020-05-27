package com.definiteplans.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.AjaxResponse;
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
    public ModelAndView editMyProfile(@RequestParam(required = false, name = "pwdupdated") Integer pwdUpdated) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView m = new ModelAndView("my_profile");
        Utils.addEnumValues(m, enumValueService, currUser);
        m.addObject("title", "Edit my Profile");
        m.addObject("selectedLanguages", currUser.getLanguageIds());
        m.addObject("user_pics", userImageRepository.findByUserId(currUser.getId()));
        m.addObject("blocked_users", userService.getBlockedUserRows());
        m.addObject("was_pwd_updated", pwdUpdated != null && pwdUpdated == 1);
        m.addObject("hasPwd", currUser.hasPwd());
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
            userService.saveUser(currUser, true);
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

            userService.saveUser(currUser, true);
        }

        return AjaxResponse.success("Saved");
    }


    @PostMapping("/settings")
    public @ResponseBody AjaxResponse saveSettings(@ModelAttribute("user") @Valid User update, BindingResult bindingResult) {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            currUser.setSendNotifications(update.isSendNotifications());
            currUser.setAgeMax(update.getAgeMax());
            currUser.setAgeMin(update.getAgeMin());
            userService.saveUser(currUser, true);
        }
        return AjaxResponse.success("Saved");
    }


    @GetMapping("/unblock/{userId}")
    public @ResponseBody AjaxResponse unblockUser(Model model, @PathVariable int userId) {
        if(!userService.unBlockUser(userId)) {
            return AjaxResponse.error(List.of("Invalid request"));
        }
        return AjaxResponse.success("UnBlocked");
    }

    @RequestMapping("/refresh-blocked-list")
    public String refreshBlockedList(Model m) {
        User currUser = userService.getCurrentUser();
        m.addAttribute("blocked_users", userService.getBlockedUserRows());
        return "edit_profile/settings :: blocked-list-frag";
    }


    @PostMapping("/delete/account")
    public @ResponseBody
    AjaxResponse deleteMyAccount(HttpServletRequest request, @ModelAttribute("deleteAccountConfirm") Boolean deleteAccountConfirm,
                                 @ModelAttribute("mypassword") String myPwd, BindingResult bindingResult) {
        User currUser = userService.getCurrentUser();
        ProfileValidator.validateDeleteAccount(currUser, bindingResult, deleteAccountConfirm, myPwd, userService);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        userService.deleteAccount();

        new SecurityContextLogoutHandler().logout(request, null, null);

        return AjaxResponse.success("Done");
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
            }
        }

        private static void validateDeleteAccount(User currUser, Errors e, Boolean deleteAccountConfirm, String myPwd, UserService userService) {
            if (BooleanUtils.isNotTrue(deleteAccountConfirm)) {
                e.reject("confirm", "Confirm that you want to delete your account.");
            }
            if (currUser.hasPwd() && StringUtils.isBlank(myPwd)) {
                e.reject("invalidPwd", "Invalid password. Please try again");
            }
            if (currUser.hasPwd() && !userService.isCorrectPwd(myPwd, currUser)) {
                e.reject("invalidPwd", "Invalid password. Please try again");
            }
        }
    }
}
