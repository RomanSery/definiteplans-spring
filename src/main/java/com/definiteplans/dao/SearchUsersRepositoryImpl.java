package com.definiteplans.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.util.DateUtil;

public class SearchUsersRepositoryImpl implements SearchUsersRepository {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Override
    public List<User> browsePagedResults(User currUser, int first, int count, SearchPrefs prefs) {
        String hql = "from User u where u.isComplete = 1 ";
        hql = hql + " and u.id not in (select bu.blockedUserId from BlockedUser bu where bu.userId = " + currUser.getId() + ")";
        hql = hql + " and " + currUser.getId() + " not in (select bu.blockedUserId from BlockedUser bu where bu.userId = u.id)";

        //TODO - this doesnt work if ageMin or ageMax = 0
        if (currUser.getDob() != null) {
            //hql = hql + " and " + DateUtil.getAge(currUser.getDob()) + " between u.ageMin and u.ageMax";
        }

        if(currUser.getAgeMin() > 0 || currUser.getAgeMax() > 0) {
            hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) between " + currUser.getAgeMin() + " and " + currUser.getAgeMax();
        }

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

        if (!StringUtils.isBlank(prefs.getState()) && !"0".equals(prefs.getState())) {
            hql = hql + " and u.state = '" + prefs.getState() + "'";
        }

        if (prefs.getEthnicties() != null && !prefs.getEthnicties().isEmpty()) {
            hql = hql + " and u.ethnicity in (" + StringUtils.join(prefs.getEthnicties(), ",") + ") ";
        }
        if (prefs.getMaritalStatuses() != null && !prefs.getMaritalStatuses().isEmpty()) {
            hql = hql + " and u.maritalStatus in (" + StringUtils.join(prefs.getMaritalStatuses(), ",") + ") ";
        }
        if (prefs.getKids() != null && !prefs.getKids().isEmpty())  {
            hql = hql + " and u.kids in (" + StringUtils.join(prefs.getKids(), ",") + ") ";
        }
        if (prefs.getWantsKids() != null && !prefs.getWantsKids().isEmpty()) {
            hql = hql + " and u.wantsKids in (" + StringUtils.join(prefs.getWantsKids(), ",") + ") ";
        }
        if (prefs.getLanguages() != null && !prefs.getLanguages().isEmpty()) {
            hql = hql + " and u.languages in (" + StringUtils.join(prefs.getLanguages(), ",") + ") ";
        }

        if (prefs.getReligions() != null && !prefs.getReligions().isEmpty()) {
            hql = hql + " and u.religion in (" + StringUtils.join(prefs.getReligions(), ",") + ") ";
        }
        if (prefs.getEducations() != null && !prefs.getEducations().isEmpty()) {
            hql = hql + " and u.education in (" + StringUtils.join(prefs.getEducations(), ",") + ") ";
        }
        if (prefs.getIncomes() != null && !prefs.getIncomes().isEmpty()) {
            hql = hql + " and u.income in (" + StringUtils.join(prefs.getIncomes(), ",") + ") ";
        }
        if (prefs.getSmokes() != null && !prefs.getSmokes().isEmpty()) {
            hql = hql + " and u.smokes in (" + StringUtils.join(prefs.getSmokes(), ",") + ") ";
        }
        if (prefs.getGenders() != null && !prefs.getGenders().isEmpty()) {
            hql = hql + " and u.gender in (" + StringUtils.join(prefs.getGenders(), ",") + ") ";
        }

        if (prefs.getDistance() != null && prefs.getDistance() > 0 && currUser.getPostalCode() != null && currUser.getPostalCode().length() > 0) {

            List<String> distanceFrom = new ArrayList<>();
            Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
            if (zip.isPresent()) {
                List<ZipCode> arr = zipCodeRepository.getZipCodesByRadius(zip.get(), prefs.getDistance());
                for (ZipCode z : arr) {
                    distanceFrom.add('\'' + z.getZip() + '\'');
                }
            }

            hql = hql + " and u.postalCode in (" + StringUtils.join(distanceFrom, ",") + ") ";
        }


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
