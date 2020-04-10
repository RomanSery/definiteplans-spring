package com.definiteplans.dao;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;

public class SearchUsersRepositoryImpl implements SearchUsersRepository {
    @Autowired
    private EntityManager entityManager;


    @Override
    public List<User> browsePagedResults(User currUser, long first, long count, Integer ageFrom, Integer ageTo, Integer heightFrom, Integer heightTo, List<String> distanceFrom, String state, List<Integer> ethnicity, List<Integer> maritalStatus, List<Integer> kids, List<Integer> wantsKids, List<Integer> languages, List<Integer> religion, List<Integer> education, List<Integer> income, List<Integer> smokes, List<Integer> gender) {
        //and u.thumbImgUrl IS NOT NULL
        String hql = "from User u where u.userStatus = 3 and u.dob IS NOT NULL and u.gender IS NOT NULL and u.state IS NOT NULL and u.postalCode IS NOT NULL ";
        hql = hql + " and u.id not in (select bu.blockedUserId from BlockedUser bu where bu.userId = " + currUser.getId() + ")";
        hql = hql + " and " + currUser.getId() + " not in (select bu.blockedUserId from BlockedUser bu where bu.userId = u.id)";

//        if (currUser.getDob() != null) hql = hql + " and " + DateUtil.getAge(currUser.getDob()) + " between u.ageMin and u.ageMax";
//
        hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) between " + currUser.getAgeMin() + " and " + currUser.getAgeMax();

        if (ageFrom != null && ageFrom.intValue() > 0)
            hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) >= " + ageFrom;
        if (ageTo != null && ageTo.intValue() > 0) hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) <= " + ageTo;

        if (heightFrom != null && heightFrom.intValue() > 0) hql = hql + " and u.height >= " + heightFrom;
        if (heightTo != null && heightTo.intValue() > 0) hql = hql + " and u.height <= " + heightTo;

        if (state != null && state.length() > 0) hql = hql + " and u.state = '" + state + "'";

        if (ethnicity != null && ethnicity.size() > 0)
            hql = hql + " and u.ethnicity in (" + StringUtils.join(ethnicity, ",") + ") ";
        if (maritalStatus != null && maritalStatus.size() > 0)
            hql = hql + " and u.maritalStatus in (" + StringUtils.join(maritalStatus, ",") + ") ";
        if (kids != null && kids.size() > 0) hql = hql + " and u.kids in (" + StringUtils.join(kids, ",") + ") ";
        if (wantsKids != null && wantsKids.size() > 0)
            hql = hql + " and u.wantsKids in (" + StringUtils.join(wantsKids, ",") + ") ";
        if (languages != null && languages.size() > 0)
            hql = hql + " and u.languages in (" + StringUtils.join(languages, ",") + ") ";

        if (religion != null && religion.size() > 0)
            hql = hql + " and u.religion in (" + StringUtils.join(religion, ",") + ") ";
        if (education != null && education.size() > 0)
            hql = hql + " and u.education in (" + StringUtils.join(education, ",") + ") ";
        if (income != null && income.size() > 0)
            hql = hql + " and u.income in (" + StringUtils.join(income, ",") + ") ";
        if (smokes != null && smokes.size() > 0)
            hql = hql + " and u.smokes in (" + StringUtils.join(smokes, ",") + ") ";
        if (gender != null && gender.size() > 0)
            hql = hql + " and u.gender in (" + StringUtils.join(gender, ",") + ") ";

        if (distanceFrom != null && distanceFrom.size() > 0)
            hql = hql + " and u.postalCode in (" + StringUtils.join(distanceFrom, ",") + ") ";


        TypedQuery<User> q = entityManager.createQuery(hql, User.class);
//        if(filters.getMaxResults() > 0) {
//            q.setMaxResults(filters.getMaxResults());
//        }
//        if(filters.getSkip() > 0) {
//            q.setFirstResult(filters.getSkip());
//        }

        return q.getResultList();
    }


    @Override
    public List<ZipCode> getZipCodesByRadius(ZipCode zip, int radius) {
        String sql = "SELECT * FROM zip_code WHERE (3958*3.1415926*sqrt((Latitude-?)*(Latitude-?) + cos(Latitude/57.29578)*cos(?/57.29578)*(Longitude-?)*(Longitude-?))/180) <= ?";

        Query q = entityManager.createNativeQuery(sql, ZipCode.class);
        return q.getResultList();

//        new Object[]{Float.valueOf(zip.getLatitude()), Float.valueOf(zip.getLatitude()),
//                Float.valueOf(zip.getLatitude()), Float.valueOf(zip.getLongitude()),
//                Float.valueOf(zip.getLongitude()), Integer.valueOf(radius)}
    }
}
