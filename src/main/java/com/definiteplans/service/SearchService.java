package com.definiteplans.service;


import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;

@Service
public class SearchService {
    private final UserRepository userRepository;

    public SearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getSearchResults(User currUser, int first, int count) {
        SearchPrefs prefs = currUser.getSearchPrefs();
        List<User> list = userRepository.browsePagedResults(currUser, first, count, prefs);
        return (list == null) ? Collections.emptyList() : list;
    }

}
