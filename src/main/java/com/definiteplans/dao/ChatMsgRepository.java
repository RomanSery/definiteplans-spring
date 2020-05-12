package com.definiteplans.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.ChatMsg;

@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, Integer> {

    @Query("FROM ChatMsg where fromId = :currUserId OR toId = :currUserId OR fromId = :profileId OR toId = :profileId order by sentDate asc")
    List<ChatMsg> getChat(@Param("currUserId") int currUserId, @Param("profileId") int profileId);
}
