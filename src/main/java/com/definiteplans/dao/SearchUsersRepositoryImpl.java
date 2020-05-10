package com.definiteplans.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

    private List<String> getDistanceFrom(User currUser, SearchPrefs prefs) {
        boolean hasDistancePref = prefs.getDistance() != null && prefs.getDistance() > 0 && currUser.getPostalCode() != null && currUser.getPostalCode().length() > 0;
        List<String> distanceFrom = null;
        if (hasDistancePref) {
            distanceFrom = new ArrayList<>();
            Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
            if (zip.isPresent()) {
                List<ZipCode> arr = zipCodeRepository.getZipCodesByRadius(zip.get(), prefs.getDistance());
                for (ZipCode z : arr) {
                    distanceFrom.add('\'' + z.getZip() + '\'');
                }
            }
        }
        return distanceFrom;
    }

    @Override
    public int getNumResults(User currUser, SearchPrefs prefs) {
        List<String> distanceFrom = getDistanceFrom(currUser, prefs);
        String hql = getHqlQuery(currUser, prefs, true, distanceFrom);

        TypedQuery<Long> q = entityManager.createQuery(hql, Long.class);
        setQueryParams(q, currUser, prefs, distanceFrom);
        return q.getSingleResult().intValue();
    }

    @Override
    public List<User> browsePagedResults(User currUser, int firstResult, int maxResults, SearchPrefs prefs) {
        List<String> distanceFrom = getDistanceFrom(currUser, prefs);
        String hql = getHqlQuery(currUser, prefs, false, distanceFrom);

        TypedQuery<User> q = entityManager.createQuery(hql, User.class);
        setQueryParams(q, currUser, prefs, distanceFrom);

        if(maxResults > 0) {
            q.setMaxResults(maxResults);
        }
        if(firstResult > 0) {
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }


    private void setQueryParams(Query q, User currUser, SearchPrefs prefs, List<String> distanceFrom) {
        q.setParameter("currUsrId", currUser.getId());

        if(currUser.getDob() != null) {
            q.setParameter("myAge", DateUtil.getAge(currUser.getDob()));
        }
        if(currUser.getAgeMin() > 0) {
            q.setParameter("myAgeMin", currUser.getAgeMin());
        }
        if(currUser.getAgeMax() > 0) {
            q.setParameter("myAgeMax", currUser.getAgeMax());
        }

        if(distanceFrom != null) {
            q.setParameter("distanceFrom", distanceFrom);
        }

        if (prefs.hasAgeFrom()) {
            q.setParameter("ageFrom", prefs.getAgeFrom());
        }
        if (prefs.hasAgeTo()) {
            q.setParameter("ageTo", prefs.getAgeTo());
        }

        if (prefs.hasHeightFrom()) {
            q.setParameter("heightFrom", prefs.getHeightFrom());
        }
        if (prefs.hasHeightTo()) {
            q.setParameter("heightTo", prefs.getHeightTo());
        }

        if (prefs.hasState()) {
            q.setParameter("state", prefs.getState());
        }
        if (prefs.hasEthnicties()) {
            q.setParameter("ethnicity", prefs.getEthnicties());
        }
        if (prefs.hasMaritalStatus()) {
            q.setParameter("maritalStatus", prefs.getMaritalStatuses());
        }
        if (prefs.hasKids()) {
            q.setParameter("kids", prefs.getKids());
        }
        if (prefs.hasWantsKids()) {
            q.setParameter("wantsKids", prefs.getWantsKids());
        }
        if (prefs.hasLanguage()) {
            q.setParameter("languages", prefs.getLanguages());
        }
        if (prefs.hasReligion()) {
            q.setParameter("religions", prefs.getReligions());
        }
        if (prefs.hasEducation()) {
            q.setParameter("educations", prefs.getEducations());
        }
        if (prefs.hasIncome()) {
            q.setParameter("incomes", prefs.getIncomes());
        }
        if (prefs.hasSmokes()) {
            q.setParameter("smokes", prefs.getSmokes());
        }
        if (prefs.hasGender()) {
            q.setParameter("genders", prefs.getGenders());
        }
    }

    private String getHqlQuery(User currUser, SearchPrefs prefs, boolean count, List<String> distanceFrom) {
        StringBuilder hql = new StringBuilder();
        if(count) {
            hql.append("select count(*) from User u where u.isComplete = 1 and u.id <> :currUsrId ");
        } else {
            hql.append("from User u where u.isComplete = 1 and u.id <> :currUsrId ");
        }

        hql.append(" and u.id not in (select bu.blockedUserId from BlockedUser bu where bu.userId = :currUsrId)");
        hql.append(" and :currUsrId not in (select bu.blockedUserId from BlockedUser bu where bu.userId = u.id)");

        if (currUser.getDob() != null) {
            hql.append(" and (u.ageMin = 0 OR :myAge >= u.ageMin) ");
            hql.append(" and (u.ageMax = 0 OR :myAge <= u.ageMax) ");
        }

        if(currUser.getAgeMin() > 0) {
            hql.append(" and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) >= :myAgeMin ");
        }
        if(currUser.getAgeMax() > 0) {
            hql.append(" and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) <= :myAgeMax ");
        }

        if (prefs.hasAgeFrom()) {
            hql.append(" and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) >= :ageFrom ");
        }
        if (prefs.hasAgeTo()) {
            hql.append(" and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) <= :ageTo ");
        }

        if (prefs.hasHeightFrom()) {
            hql.append(" and u.height >= :heightFrom ");
        }
        if (prefs.hasHeightTo()) {
            hql.append(" and u.height <= :heightTo ");
        }

        if (prefs.hasState()) {
            hql.append(" and u.state = :state ");
        }

        if (prefs.hasEthnicties()) {
            hql.append(" and u.ethnicity in (:ethnicity) ");
        }
        if (prefs.hasMaritalStatus()) {
            hql.append(" and u.maritalStatus in (:maritalStatus) ");
        }
        if (prefs.hasKids()) {
            hql.append(" and u.kids in (:kids) ");
        }
        if (prefs.hasWantsKids()) {
            hql.append(" and u.wantsKids in (:wantsKids) ");
        }
        if (prefs.hasLanguage()) {
            hql.append(" and u.languages in (:languages) ");
        }

        if (prefs.hasReligion()) {
            hql.append(" and u.religion in (:religions) ");
        }
        if (prefs.hasEducation()) {
            hql.append(" and u.education in (:educations) ");
        }
        if (prefs.hasIncome()) {
            hql.append(" and u.income in (:incomes) ");
        }
        if (prefs.hasSmokes()) {
            hql.append(" and u.smokes in (:smokes) ");
        }
        if (prefs.hasGender()) {
            hql.append(" and u.gender in (:genders) ");
        }

        if (distanceFrom != null) {
            hql.append(" and u.postalCode in (:distanceFrom) ");
        }

        hql.append(" order by u.lastLoginDate desc");
        return hql.toString();
    }
}
