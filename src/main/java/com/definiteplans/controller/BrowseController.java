package com.definiteplans.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import com.definiteplans.controller.model.Pagination;
import com.definiteplans.controller.model.SearchResult;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;
import com.definiteplans.service.DateService;
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
    private final DateService dateService;
    private final DefiniteDateRepository definiteDateRepository;

    private static final int numPerPage = 8;

    public BrowseController(UserService userService, SearchService searchService, EnumValueService enumValueService, DateService dateService, DefiniteDateRepository definiteDateRepository) {
        this.userService = userService;
        this.searchService = searchService;
        this.enumValueService = enumValueService;
        this.dateService = dateService;
        this.definiteDateRepository = definiteDateRepository;
    }

    @GetMapping("/browse")
    public ModelAndView browse(@RequestParam(required = false, name = "blocked") Integer blocked) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return new ModelAndView(new RedirectView("/"));
        }

        ModelAndView m = new ModelAndView("browse");
        m.addObject("was_blocked", blocked != null && blocked == 1);
        m.addObject("title", "Browse Profiles");
        Utils.addEnumValues(m, enumValueService, currUser);
        m.addObject("prefs", currUser.getSearchPrefs());

        List<SearchResult> results = getSearchResults(currUser, 0);
        int numResults = searchService.getNumSearchResults(currUser);
        m.addObject("profiles", results);
        m.addObject("numResults", numResults);
        m.addObject("pagination", getPagination(0, numResults));
        return m;
    }

    @PostMapping("/browse/filter")
    public @ResponseBody AjaxResponse applyFilters(@ModelAttribute("prefs") SearchPrefs prefs) {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            currUser.setSearchPrefs(prefs);
            userService.saveUser(currUser, true);
        }

        return AjaxResponse.success("Saved");
    }

    @GetMapping("/browse/reset")
    public ModelAndView clearFilters() {
        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            currUser.setSearchPrefs(new SearchPrefs());
            userService.saveUser(currUser, true);
        }

        return new ModelAndView(new RedirectView("/browse"));
    }


    @RequestMapping(value = {"/refresh-browse-results", "/refresh-browse-results/{pageNum}"})
    public String refreshBrowsingResults(ModelMap m, @PathVariable(name = "pageNum", required = false) Optional<Integer> pageNum) {
        User currUser = userService.getCurrentUser();
        List<SearchResult> results = getSearchResults(currUser, pageNum.isPresent() ? pageNum.get() : 0);
        int numResults = searchService.getNumSearchResults(currUser);
        m.addAttribute("profiles", results);
        m.addAttribute("numResults", numResults);
        m.addAttribute("pagination", getPagination(pageNum.isPresent() ? pageNum.get() : 0, numResults));
        return "browse :: search-results";
    }

    private List<SearchResult> getSearchResults(User currUser, Integer page) {

        List<User> profiles = searchService.getSearchResults(currUser, numPerPage, page);
        List<SearchResult> searchResults = new ArrayList<>(profiles.size());
        for(User u : profiles) {

            Integer age = (u.getDob() != null) ? DateUtil.getAge(u.getDob()) : null;
            if (age != null && age.intValue() < 18) age = null;
            String name = u.getDisplayName() + ((age != null) ? (", " + age) : "");

            DefiniteDate activeDate = definiteDateRepository.getActiveDate(currUser.getId(), u.getId());
            Boolean b = dateService.wantsMore(currUser, u, activeDate);
            boolean isOnline = UserService.isOnlineNow(u);

            SearchResult sr = new SearchResult(u.getId(), userService.getProfileImg(u, true), name, UserService.getAddrDesc(u), u.getNumNoShows(),
                    BooleanUtils.isTrue(b), b != null && !b.booleanValue(), activeDate != null, isOnline);
            searchResults.add(sr);
        }

        return searchResults;
    }


    private Pagination getPagination(Integer page, int numResults) {
        int totalPages = (numResults + numPerPage - 1) / numPerPage;

        if(page == null) {
            page = 0;
        }

        boolean showPrev = page > 0;
        boolean showNext = page < (totalPages - 1);
        int currPage = page;
        int prevPage = page - 1;
        int nextPage = page + 1;

        return new Pagination(prevPage, currPage, nextPage, showPrev, showNext, totalPages);
    }
}
