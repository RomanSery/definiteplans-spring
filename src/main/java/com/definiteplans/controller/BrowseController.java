package com.definiteplans.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.SearchResult;
import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.State;
import com.definiteplans.service.DefiniteDateService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;
import com.definiteplans.util.Utils;

@Controller
public class BrowseController {
    private final UserService userService;
    private final EnumValueRepository enumValueRepository;
    private final DefiniteDateService definiteDateService;

    public BrowseController(UserService userService, EnumValueRepository enumValueRepository, DefiniteDateService definiteDateService) {
        this.userService = userService;
        this.enumValueRepository = enumValueRepository;
        this.definiteDateService = definiteDateService;
    }

    @GetMapping("/browse")
    public ModelAndView browse() {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView m = new ModelAndView("browse");
        Utils.addEnumValues(m, enumValueRepository, currUser);
        m.addObject("selectedSmokeTypes", currUser.getMultiPref("smokes"));

        List<User> profiles = userService.getSearchResults(currUser);
        List<SearchResult> searchResults = new ArrayList<>(profiles.size());
        for(User u : profiles) {

            Integer age = (u.getDob() != null) ? DateUtil.getAge(u.getDob()) : null;
            if (age != null && age.intValue() < 18) age = null;
            String name = u.getDisplayName() + ((age != null) ? (", " + age) : "");

            DefiniteDate activeDate = definiteDateService.getActiveDate(currUser, u);
            Boolean b = wantsMore(currUser, u, activeDate);

            SearchResult sr = new SearchResult(userService.getProfileImg(u, true), name, userService.getAddrDesc(u), u.getNumNoShows(), BooleanUtils.isTrue(b), activeDate != null);
            searchResults.add(sr);
        }

        m.addObject("profiles", searchResults);


        return m;
    }

    private Boolean wantsMore(User currUser, User viewing, DefiniteDate activeDate) {
        if (activeDate != null) return null;
        List<DefiniteDate> pastDates = definiteDateService.getPastDatesByOwnerAndDatee(currUser, viewing);
        if (pastDates.size() > 0) {
            DefiniteDate dd = pastDates.get(0);
            if (viewing.getId() == dd.getOwnerUserId()) {
                return Boolean.valueOf(dd.isOwnerWantsMore());
            }
            return Boolean.valueOf(dd.isDateeWantsMore());
        }
        return null;
    }

}
