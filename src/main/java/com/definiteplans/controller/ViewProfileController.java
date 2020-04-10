package com.definiteplans.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.dao.BlockedUserRepository;
import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.BlockedUser;
import com.definiteplans.dom.User;
import com.definiteplans.service.DefiniteDateService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@Controller
public class ViewProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EnumValueRepository enumValueRepository;
    private final DefiniteDateService definiteDateService;
    private final BlockedUserRepository blockedUserRepository;

    public ViewProfileController(UserService userService, UserRepository userRepository, EnumValueRepository enumValueRepository, DefiniteDateService definiteDateService, BlockedUserRepository blockedUserRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.enumValueRepository = enumValueRepository;
        this.definiteDateService = definiteDateService;
        this.blockedUserRepository = blockedUserRepository;
    }

    @GetMapping("/profiles/")
    public ModelAndView viewProfile() {
        return new ModelAndView(new RedirectView("/browse"));
    }


    @GetMapping("/profiles/{userId}")
    public ModelAndView viewProfile(@PathVariable("userId") Integer userId) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }


        Optional<User> found = userRepository.findById(userId);
        if(found.isEmpty()) {
            return new ModelAndView(new RedirectView("/browse"));
        }

        User profile = found.get();
        boolean isViewingSelf = currUser.getId() == profile.getId();


        if (!isViewingSelf) {
            List<BlockedUser> arr = blockedUserRepository.findByUserId(profile.getId());
            for (BlockedUser bu : arr) {
                if (bu.getBlockedUserId() == currUser.getId()) {
                    return new ModelAndView(new RedirectView("/browse"));
                }
            }
            arr = blockedUserRepository.findByUserId(currUser.getId());
            for (BlockedUser bu : arr) {
                if (bu.getBlockedUserId() == profile.getId()) {
                    return new ModelAndView(new RedirectView("/browse"));
                }
            }

            int myAge = DateUtil.getAge(currUser.getDob());
            if (myAge > profile.getAgeMax() || myAge < profile.getAgeMin()) {
                return new ModelAndView(new RedirectView("/browse"));
            }
            int profileAge = DateUtil.getAge(profile.getDob());
            if (profileAge > currUser.getAgeMax() || profileAge < currUser.getAgeMin()) {
                return new ModelAndView(new RedirectView("/browse"));
            }
        }




        ModelAndView m = new ModelAndView("view_profile");


        return m;
    }


}
