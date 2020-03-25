package com.definiteplans.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.BlockedUserRepository;
import com.definiteplans.dom.BlockedUser;


@Service
public class BlockedUserService {
    @Autowired private BlockedUserRepository blockedUserRepository;

    public List<BlockedUser> getByUser(Integer userId) {
        if(userId == null) {
            return Collections.emptyList();
        }
        List<BlockedUser> arr = blockedUserRepository.findByUserId(userId);
        return (arr != null) ? arr : Collections.emptyList();
    }

    public void delete(BlockedUser bu) {
        blockedUserRepository.delete(bu);
    }

    public boolean save(BlockedUser bu) {
        if (bu == null) {
            return false;
        }

        blockedUserRepository.save(bu);
        return true;
    }
}
