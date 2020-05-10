package com.definiteplans.dao;


import java.util.List;

import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;

public interface SearchUsersRepository {

    List<User> browsePagedResults(User currUser, int firstResult, int maxResults, SearchPrefs prefs);
    int getNumResults(User currUser, SearchPrefs prefs);
}
