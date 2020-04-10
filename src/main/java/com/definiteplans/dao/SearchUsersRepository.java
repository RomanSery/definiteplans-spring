package com.definiteplans.dao;


import java.util.List;

import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;

public interface SearchUsersRepository {

    List<User> browsePagedResults(User currUser, long first, long count,
                                  Integer ageFrom, Integer ageTo, Integer heightFrom, Integer heightTo, List<String> distanceFrom, String state,
                                  List<Integer> ethnicity, List<Integer> maritalStatus, List<Integer> kids, List<Integer> wantsKids, List<Integer> languages,
                                  List<Integer> religion, List<Integer> education, List<Integer> income, List<Integer> smokes, List<Integer> gender);


    List<ZipCode> getZipCodesByRadius(ZipCode zip, int radius);
}
