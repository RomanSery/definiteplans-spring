package com.definiteplans.controller;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.ProfileUpdate;
import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;

@Controller
@RequestMapping(path="/me")
public class MyProfileController {
    private final UserService userService;

    public MyProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView editMyProfile(Model model) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView m = new ModelAndView("my_profile");
        m.addObject("user", new ProfileUpdate(currUser));

        return m;
    }

    @PostMapping("/profile")
    public ModelAndView saveMyProfile(@ModelAttribute("user") @Valid ProfileUpdate user, BindingResult bindingResult) {
        ProfileValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("my_profile", Map.of("user", user));
        }

        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }
        currUser.setDisplayName(user.getDisplayName());
        userService.updateUser(currUser);

        return new ModelAndView(new RedirectView("/me/profile"));
    }


    @GetMapping("/pwd")
    public ModelAndView showUpdatePasswordPage(Model model) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }
        return new ModelAndView("update_pwd", Map.of("user", new PwdUpdate(), "hasPwd", currUser.hasPwd()));
    }

    @PostMapping("/pwd")
    public ModelAndView updateMyPassword(@ModelAttribute("user") @Valid PwdUpdate pwd, BindingResult bindingResult) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ProfileValidator.validate(pwd, bindingResult, userService, currUser);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("update_pwd", Map.of("user", pwd, "hasPwd", currUser.hasPwd()));
        }

        boolean success = userService.changeUserPassword(currUser, pwd);
        if (!success) {
            return new ModelAndView("update_pwd", Map.of("user", pwd, "hasPwd", currUser.hasPwd()));
        }
        return new ModelAndView(new RedirectView("/me/profile"));
    }


    private static class ProfileValidator {
        private static void validate(ProfileUpdate obj, Errors e) {
            ValidationUtils.rejectIfEmpty(e, "displayName", "displayName.empty", "Display name is required");
        }

        private static void validate(PwdUpdate obj, Errors e, UserService userService, User user) {
            if(user.hasPwd()) {
                ValidationUtils.rejectIfEmpty(e, "currPwd", "", "Current password is required");
                if(!userService.isCorrectPwd(obj.getCurrPwd(), user)) {
                    e.reject("currPwdField.mismatch", "Current password is incorrect");
                }
            }

            ValidationUtils.rejectIfEmpty(e, "password1", "", "New password is required");
            ValidationUtils.rejectIfEmpty(e, "password2", "", "Confirm new password");

            if(!obj.getPassword1().equals(obj.getPassword2())) {
                e.reject("pwd.mismatch", "Passwords must match");
            }
        }
    }
}
