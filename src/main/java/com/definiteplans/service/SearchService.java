package com.definiteplans.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.util.Utils;

@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    private final UserRepository userRepository;
    private final ZipCodeService zipCodeService;

    public SearchService(UserRepository userRepository, ZipCodeService zipCodeService) {
        this.userRepository = userRepository;
        this.zipCodeService = zipCodeService;
    }

    public List<User> browsePagedResults(User currUser, long first, long count, Integer ageFrom, Integer ageTo, Integer heightFrom,
                                         Integer heightTo, List<String> distanceFrom, String state, List<Integer> ethnicity,
                                         List<Integer> maritalStatus, List<Integer> kids,
                                         List<Integer> wantsKids, List<Integer> languages,
                                         List<Integer> religion, List<Integer> education, List<Integer> income,
                                         List<Integer> smokes, List<Integer> gender) {
        List<User> list = userRepository.browsePagedResults(currUser, first, count, ageFrom, ageTo, heightFrom, heightTo,
                distanceFrom, state, ethnicity, maritalStatus, kids, wantsKids, languages, religion, education, income, smokes, gender);
        return (list == null) ? Collections.emptyList() : list;
    }

    public List<User> getSearchResults(User currUser) {
        Integer ageFrom = currUser.getSinglePrefIntVal("ageFrom");
        Integer ageTo = currUser.getSinglePrefIntVal("ageTo");
        Integer heightFrom = currUser.getSinglePrefIntVal("heightFrom");
        Integer heightTo = currUser.getSinglePrefIntVal("heightTo");
        String state = (currUser.getSinglePrefState("name") != null) ? currUser.getSinglePrefState("name").getAbbreviation() : null;

        List<Integer> ethnicity = Utils.getFilterValues2(currUser.getMultiPref("ethnicity"));
        List<Integer> maritalStatus = Utils.getFilterValues2(currUser.getMultiPref("maritalStatus"));
        List<Integer> kids = Utils.getFilterValues2(currUser.getMultiPref("kids"));
        List<Integer> wantsKids = Utils.getFilterValues2(currUser.getMultiPref("wantsKids"));
        List<Integer> languages = Utils.getFilterValues2(currUser.getMultiPref("languages"));
        List<Integer> religion = Utils.getFilterValues2(currUser.getMultiPref("religion"));
        List<Integer> education = Utils.getFilterValues2(currUser.getMultiPref("education"));
        List<Integer> income = Utils.getFilterValues2(currUser.getMultiPref("income"));
        List<Integer> smokes = Utils.getFilterValues2(currUser.getMultiPref("smokes"));
        List<Integer> gender = Utils.getFilterValues2(currUser.getMultiPref("gender"));
        Integer distance = currUser.getSinglePrefIntVal("distanceFrom");

        List<String> distanceFrom = new ArrayList<>();
        if (distance != null && distance.intValue() > 0 && currUser.getPostalCode() != null && currUser.getPostalCode().length() > 0) {
            ZipCode zip = this.zipCodeService.getById(currUser.getPostalCode());
            if (zip != null) {
                List<ZipCode> arr = this.zipCodeService.getZipCodesByRadius(zip, distance.intValue());
                for (ZipCode z : arr) distanceFrom.add("'" + z.getZip() + "'");

            }
        }

        return browsePagedResults(currUser, 0L, 0L, ageFrom, ageTo, heightFrom, heightTo, distanceFrom, state, ethnicity, maritalStatus, kids, wantsKids, languages, religion, education, income, smokes, gender);
    }

}
