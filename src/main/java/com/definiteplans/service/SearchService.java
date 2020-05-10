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

    public List<User> getSearchResults(User currUser, int numPerPage, Integer page) {
        if(page == null) {
            page = 0;
        }
        int firstResult = page * numPerPage;

        SearchPrefs prefs = currUser.getSearchPrefs();
        List<User> list = userRepository.browsePagedResults(currUser, firstResult, numPerPage, prefs);
        return (list == null) ? Collections.emptyList() : list;
    }

    public int getNumSearchResults(User currUser) {
        SearchPrefs prefs = currUser.getSearchPrefs();
        return userRepository.getNumResults(currUser, prefs);
    }

}
