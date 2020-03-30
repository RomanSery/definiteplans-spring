package com.definiteplans.controller;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dom.User;
import com.definiteplans.service.UserService;

@Controller
public class UpdatePwdController {
    private final UserService userService;

    public UpdatePwdController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/pwd")
    public ModelAndView updatePwdPage(Model model) {
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
