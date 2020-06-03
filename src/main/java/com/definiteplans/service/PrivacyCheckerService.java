package com.definiteplans.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.BlockedUserRepository;
import com.definiteplans.dom.BlockedUser;
import com.definiteplans.dom.User;
import com.definiteplans.util.DateUtil;

@Service
public class PrivacyCheckerService {
    private static final Logger logger = LoggerFactory.getLogger(PrivacyCheckerService.class);

    private final BlockedUserRepository blockedUserRepository;

    public PrivacyCheckerService(BlockedUserRepository blockedUserRepository) {
        this.blockedUserRepository = blockedUserRepository;
    }

    public boolean canViewProfile(User currUser, User profile) {
        if(profile == null) {
            return false;
        }
        boolean isViewingSelf = currUser.getId() == profile.getId();
        if(isViewingSelf) {
            return true;
        }

        List<BlockedUser> arr = blockedUserRepository.findByUserId(profile.getId());
        for (BlockedUser bu : arr) {
            if (bu.getBlockedUserId() == currUser.getId()) {
                return false;
            }
        }
        arr = blockedUserRepository.findByUserId(currUser.getId());
        for (BlockedUser bu : arr) {
            if (bu.getBlockedUserId() == profile.getId()) {
                return false;
            }
        }

        int myAge = DateUtil.getAge(currUser.getDob());
        if ( (profile.getAgeMax() > 0 && myAge > profile.getAgeMax()) || (myAge < profile.getAgeMin())) {
            return false;
        }
        int profileAge = DateUtil.getAge(profile.getDob());
        if ( (currUser.getAgeMax() > 0 && profileAge > currUser.getAgeMax()) || profileAge < currUser.getAgeMin()) {
            return false;
        }

        return true;
    }

}
