package com.definiteplans.dao;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;

public class SearchUsersRepositoryImpl implements SearchUsersRepository {
    @Autowired
    private EntityManager entityManager;

    /*
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
            Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
            if (zip.isPresent()) {
                List<ZipCode> arr = zipCodeRepository.getZipCodesByRadius(zip.get(), distance.intValue());
                for (ZipCode z : arr) {
                    distanceFrom.add('\'' + z.getZip() + '\'');
                }
            }
        }
     */

    @Override
    public List<User> browsePagedResults(User currUser, int first, int count, SearchPrefs prefs) {
        //and u.thumbImgUrl IS NOT NULL
        String hql = "from User u where u.userStatus = 3 and u.dob IS NOT NULL and u.gender IS NOT NULL and u.state IS NOT NULL and u.postalCode IS NOT NULL ";
        hql = hql + " and u.id not in (select bu.blockedUserId from BlockedUser bu where bu.userId = " + currUser.getId() + ")";
        hql = hql + " and " + currUser.getId() + " not in (select bu.blockedUserId from BlockedUser bu where bu.userId = u.id)";

//        if (currUser.getDob() != null) hql = hql + " and " + DateUtil.getAge(currUser.getDob()) + " between u.ageMin and u.ageMax";
//
        hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) between " + currUser.getAgeMin() + " and " + currUser.getAgeMax();

        if (prefs.getAgeFrom() != null && prefs.getAgeFrom().intValue() > 0) {
            hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) >= " + prefs.getAgeFrom();
        }
        if (prefs.getAgeTo() != null && prefs.getAgeTo() > 0) {
            hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) <= " + prefs.getAgeTo();
        }

        if (prefs.getHeightFrom() != null && prefs.getHeightFrom() > 0) {
            hql = hql + " and u.height >= " + prefs.getHeightFrom();
        }
        if (prefs.getHeightTo() != null && prefs.getHeightTo() > 0) {
            hql = hql + " and u.height <= " + prefs.getHeightTo();
        }

        if (prefs.getState() != null && prefs.getState().length() > 0) {
            hql = hql + " and u.state = '" + prefs.getState() + "'";
        }

        if (prefs.getEthnicties() != null && prefs.getEthnicties().size() > 0) {
            hql = hql + " and u.ethnicity in (" + StringUtils.join(prefs.getEthnicties(), ",") + ") ";
        }
        if (prefs.getMaritalStatuses() != null && prefs.getMaritalStatuses().size() > 0) {
            hql = hql + " and u.maritalStatus in (" + StringUtils.join(prefs.getMaritalStatuses(), ",") + ") ";
        }
        if (prefs.getKids() != null && prefs.getKids().size() > 0)  {
            hql = hql + " and u.kids in (" + StringUtils.join(prefs.getKids(), ",") + ") ";
        }
        if (prefs.getWantsKids() != null && prefs.getWantsKids().size() > 0) {
            hql = hql + " and u.wantsKids in (" + StringUtils.join(prefs.getWantsKids(), ",") + ") ";
        }
        if (prefs.getLanguages() != null && prefs.getLanguages().size() > 0) {
            hql = hql + " and u.languages in (" + StringUtils.join(prefs.getLanguages(), ",") + ") ";
        }

        if (prefs.getReligions() != null && !prefs.getReligions().isEmpty()) {
            hql = hql + " and u.religion in (" + StringUtils.join(prefs.getReligions(), ",") + ") ";
        }
        if (prefs.getEducations() != null && prefs.getEducations().size() > 0) {
            hql = hql + " and u.education in (" + StringUtils.join(prefs.getEducations(), ",") + ") ";
        }
        if (prefs.getIncomes() != null && prefs.getIncomes().size() > 0) {
            hql = hql + " and u.income in (" + StringUtils.join(prefs.getIncomes(), ",") + ") ";
        }
        if (prefs.getSmokes() != null && prefs.getSmokes().size() > 0) {
            hql = hql + " and u.smokes in (" + StringUtils.join(prefs.getSmokes(), ",") + ") ";
        }
        if (prefs.getGenders() != null && prefs.getGenders().size() > 0) {
            hql = hql + " and u.gender in (" + StringUtils.join(prefs.getGenders(), ",") + ") ";
        }

//        if (distanceFrom != null && distanceFrom.size() > 0) {
//            hql = hql + " and u.postalCode in (" + StringUtils.join(distanceFrom, ",") + ") ";
//        }


        TypedQuery<User> q = entityManager.createQuery(hql, User.class);
        if(first > 0) {
            q.setMaxResults(first);
        }
        if(count > 0) {
            q.setFirstResult(count);
        }

        return q.getResultList();
    }

}
