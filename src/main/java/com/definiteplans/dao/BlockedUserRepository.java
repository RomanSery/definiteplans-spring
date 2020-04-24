package com.definiteplans.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.BlockedUser;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Integer> {

    List<BlockedUser> findByUserId(@Param("userId") int userId);
    BlockedUser findByUserIdAndBlockedUserId(@Param("userId") int userId, @Param("blockedUserId") int blockedUserId);
    long countByUserIdAndBlockedUserId(@Param("userId") int userId, @Param("blockedUserId") int blockedUserId);

}
