package com.definiteplans.controller;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.PwdUpdate;
import com.definiteplans.dom.User;

@Controller
public class LoginController {

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView m = new ModelAndView("login");
        m.addObject("title", "Login");
        return m;
    }

//    @PostMapping("/login")
//    public ModelAndView doLogin(@ModelAttribute("user") @Valid PwdUpdate pwd, BindingResult bindingResult) {
//        User currUser = userService.getCurrentUser();
//        if(currUser == null) {
//            return new ModelAndView(new RedirectView("/"));
//        }
//
//        MyProfileController.ProfileValidator.validate(pwd, bindingResult, userService, currUser);
//        if (bindingResult.hasErrors()) {
//            return new ModelAndView("update_pwd", Map.of("user", pwd, "hasPwd", currUser.hasPwd()));
//        }
//
//        boolean success = userService.changeUserPassword(currUser, pwd);
//        if (!success) {
//            return new ModelAndView("update_pwd", Map.of("user", pwd, "hasPwd", currUser.hasPwd()));
//        }
//        return new ModelAndView(new RedirectView("/me/profile"));
//    }

}
