package com.definiteplans.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.EnumValue;
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

    public ViewProfileController(UserService userService, UserRepository userRepository, EnumValueRepository enumValueRepository, DefiniteDateService definiteDateService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.enumValueRepository = enumValueRepository;
        this.definiteDateService = definiteDateService;
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
        Integer age = (profile.getDob() != null) ? DateUtil.getAge(profile.getDob()) : null;
        if (age != null && age.intValue() < 18) age = Integer.valueOf(18);

        List<String> languages = new ArrayList<>();
        if (profile.getLanguages() != null && profile.getLanguages().length() > 0) {
            for (String languageId : profile.getLanguages().split(",")) {
                languages.add(getProfileVal(Integer.parseInt(languageId)));
            }
        }

        ModelAndView m = new ModelAndView("view_profile");
        m.addObject("isViewingSelf", isViewingSelf);
        m.addObject("profile", profile);
        m.addObject("loc", userService.getAddrDesc(profile));
        m.addObject("profileThumbImg", userService.getProfileImg(profile, true));
        m.addObject("age", age);
        m.addObject("gender", getProfileVal(profile.getGender()));
        m.addObject("height", getProfileVal(profile.getHeight()));
        m.addObject("ethnicity", getProfileVal(profile.getEthnicity()));
        m.addObject("maritalStatus", getProfileVal(profile.getMaritalStatus()));
        m.addObject("kids", getProfileVal(profile.getKids()));
        m.addObject("wantsKids", getProfileVal(profile.getWantsKids()));
        m.addObject("languages", StringUtils.join(languages, ","));
        m.addObject("religion", getProfileVal(profile.getReligion()));
        m.addObject("education", getProfileVal(profile.getEducation()));
        m.addObject("income", getProfileVal(profile.getIncome()));
        m.addObject("smokes", getProfileVal(profile.getSmokes()));
        m.addObject("aboutMe", profile.getAboutMePretty());
        m.addObject("interests", profile.getInterestsPretty());
        return m;
    }


    private String getProfileVal(int n) {
        if(n == 0) {
            return "";
        }
        Optional<EnumValue> enumValue = enumValueRepository.findById(n);
        return enumValue.isPresent() ? enumValue.get().getEnumValue() : "";
    }

}
