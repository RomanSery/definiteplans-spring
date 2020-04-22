package com.definiteplans.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import com.definiteplans.dom.SearchPrefs;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;

public class SearchUsersRepositoryImpl implements SearchUsersRepository {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ZipCodeRepository zipCodeRepository;

    @Override
    public List<User> browsePagedResults(User currUser, int first, int count, SearchPrefs prefs) {
        StringBuilder hql = new StringBuilder();
        hql.append("from User u where u.isComplete = 1 ");
        hql.append(" and u.id not in (select bu.blockedUserId from BlockedUser bu where bu.userId = :currUsrId)");
        hql.append(" and :currUsrId not in (select bu.blockedUserId from BlockedUser bu where bu.userId = u.id)");

        //TODO - this doesnt work if ageMin or ageMax = 0
        if (currUser.getDob() != null) {
            //hql = hql + " and " + DateUtil.getAge(currUser.getDob()) + " between u.ageMin and u.ageMax";
        }

//        if(currUser.getAgeMin() > 0 || currUser.getAgeMax() > 0) {
//            hql = hql + " and TIMESTAMPDIFF(YEAR,u.dob,CURDATE()) between " + currUser.getAgeMin() + " and " + currUser.getAgeMax();
//        }
//
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

        boolean hasDistancePref = prefs.getDistance() != null && prefs.getDistance() > 0 && currUser.getPostalCode() != null && currUser.getPostalCode().length() > 0;
        List<String> distanceFrom = new ArrayList<>();
        if (hasDistancePref) {
            Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
            if (zip.isPresent()) {
                List<ZipCode> arr = zipCodeRepository.getZipCodesByRadius(zip.get(), prefs.getDistance());
                for (ZipCode z : arr) {
                    distanceFrom.add('\'' + z.getZip() + '\'');
                }
            }
            hql.append(" and u.postalCode in (:distanceFrom) ");
        }


        TypedQuery<User> q = entityManager.createQuery(hql.toString(), User.class);
        q.setParameter("currUsrId", currUser.getId());

        if(hasDistancePref) {
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






        if(first > 0) {
            q.setMaxResults(first);
        }
        if(count > 0) {
            q.setFirstResult(count);
        }

        return q.getResultList();
    }

}
