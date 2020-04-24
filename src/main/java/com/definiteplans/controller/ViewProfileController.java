package com.definiteplans.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.dao.UserImageRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.service.DefiniteDateService;
import com.definiteplans.service.EnumValueService;
import com.definiteplans.service.UserService;

@Controller
public class ViewProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final EnumValueService enumValueService;
    private final DefiniteDateService definiteDateService;
    private final UserImageRepository userImageRepository;

    public ViewProfileController(UserService userService, UserRepository userRepository, EnumValueService enumValueService, DefiniteDateService definiteDateService, UserImageRepository userImageRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.enumValueService = enumValueService;
        this.definiteDateService = definiteDateService;
        this.userImageRepository = userImageRepository;
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
        if(!userService.canViewProfile(currUser, profile)) {
            return new ModelAndView(new RedirectView("/browse"));
        }


        boolean isViewingSelf = currUser.getId() == profile.getId();

        ModelAndView m = new ModelAndView("view_profile");
        m.addObject("title", "View Profile - " + profile.getDisplayName());
        m.addObject("isViewingSelf", isViewingSelf);
        m.addObject("profile", profile);
        m.addObject("loc", userService.getAddrDesc(profile));
        m.addObject("profileThumbImg", userService.getProfileImg(profile, true));
        m.addObject("profileFullImg", userService.getProfileImg(profile, false));
        m.addObject("age", userService.getProfileAge(profile));
        m.addObject("gender", getProfileVal(profile.getGender()));
        m.addObject("height", getProfileVal(profile.getHeight()));
        m.addObject("ethnicity", getProfileVal(profile.getEthnicity()));
        m.addObject("maritalStatus", getProfileVal(profile.getMaritalStatus()));
        m.addObject("kids", getProfileVal(profile.getKids()));
        m.addObject("wantsKids", getProfileVal(profile.getWantsKids()));
        m.addObject("languages", getLanguages(profile));
        m.addObject("religion", getProfileVal(profile.getReligion()));
        m.addObject("education", getProfileVal(profile.getEducation()));
        m.addObject("income", getProfileVal(profile.getIncome()));
        m.addObject("smokes", getProfileVal(profile.getSmokes()));
        m.addObject("aboutMe", profile.getAboutMePretty());
        m.addObject("interests", profile.getInterestsPretty());

        m.addObject("user_pics", userImageRepository.findByUserId(profile.getId()));
        return m;
    }


    @GetMapping("/profiles/block/{userId}")
    public @ResponseBody AjaxResponse blockUser(Model model, @PathVariable int userId) {
        if(!userService.blockUser(userId)) {
            return AjaxResponse.error(List.of("Invalid request"));
        }
        return AjaxResponse.success("Blocked");
    }

    private String getProfileVal(int n) {
        if(n == 0) {
            return "";
        }
        EnumValue enumValue = enumValueService.findById(n);
        return enumValue != null ? enumValue.getEnumValue() : "";
    }

    private String getLanguages(User profile) {
        List<String> languages = new ArrayList<>();
        if (profile.getLanguages() != null && profile.getLanguages().length() > 0) {
            for (String languageId : profile.getLanguages().split(",")) {
                languages.add(getProfileVal(Integer.parseInt(languageId)));
            }
        }
        return StringUtils.join(languages, ",");
    }
}
