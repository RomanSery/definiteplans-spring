package com.definiteplans.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.controller.model.SearchResult;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;
import com.definiteplans.service.DefiniteDateService;
import com.definiteplans.service.EnumValueService;
import com.definiteplans.service.SearchService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;
import com.definiteplans.util.Utils;

@Controller
public class BrowseController {
    private final UserService userService;
    private final SearchService searchService;
    private final EnumValueService enumValueService;
    private final DefiniteDateService definiteDateService;

    public BrowseController(UserService userService, SearchService searchService, EnumValueService enumValueService, DefiniteDateService definiteDateService) {
        this.userService = userService;
        this.searchService = searchService;
        this.enumValueService = enumValueService;
        this.definiteDateService = definiteDateService;
    }

    @GetMapping("/browse")
    public ModelAndView browse() {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView m = new ModelAndView("browse");
        Utils.addEnumValues(m, enumValueService, currUser);
        m.addObject("prefs", currUser.getSearchPrefs());

        List<SearchResult> results = getSearchResults(currUser);
        m.addObject("profiles", results);
        m.addObject("numResults", results.size());
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


    @PostMapping("/browse/filter")
    public @ResponseBody AjaxResponse applyFilters(@ModelAttribute("prefs") SearchPrefs prefs) {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            currUser.setSearchPrefs(prefs);
            userService.saveUser(currUser);
        }

        return AjaxResponse.success("Saved");
    }

    @GetMapping("/browse/reset")
    public ModelAndView clearFilters() {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            currUser.setSearchPrefs(new SearchPrefs());
            userService.saveUser(currUser);
        }

        return new ModelAndView(new RedirectView("/browse"));
    }


    @RequestMapping("/refresh-browse-results")
    public String refreshBrowsingResults(Model m) {
        User currUser = userService.getCurrentUser();
        List<SearchResult> results = getSearchResults(currUser);
        m.addAttribute("profiles", results);
        m.addAttribute("numResults", results.size());
        return "browse :: search-results";
    }

    private List<SearchResult> getSearchResults( User currUser) {

        List<User> profiles = searchService.getSearchResults(currUser, 0, 0);
        List<SearchResult> searchResults = new ArrayList<>(profiles.size());
        for(User u : profiles) {

            Integer age = (u.getDob() != null) ? DateUtil.getAge(u.getDob()) : null;
            if (age != null && age.intValue() < 18) age = null;
            String name = u.getDisplayName() + ((age != null) ? (", " + age) : "");

            DefiniteDate activeDate = definiteDateService.getActiveDate(currUser, u);
            Boolean b = wantsMore(currUser, u, activeDate);

            SearchResult sr = new SearchResult(u.getId(), userService.getProfileImg(u, true), name, userService.getAddrDesc(u), u.getNumNoShows(),
                    BooleanUtils.isTrue(b), b != null && !b.booleanValue(), activeDate != null);
            searchResults.add(sr);
        }

        return searchResults;
    }
}
