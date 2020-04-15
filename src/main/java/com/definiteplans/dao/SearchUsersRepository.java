package com.definiteplans.dao;


import java.util.List;

import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;

public interface SearchUsersRepository {

    List<User> browsePagedResults(User currUser, int first, int count, SearchPrefs prefs);
}
