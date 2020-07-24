package com.definiteplans.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.definiteplans.dom.ChatMsg;

@Repository
public interface ChatMsgRepository extends JpaRepository<ChatMsg, Integer> {

    @Query("FROM ChatMsg where toId = :currUserId and isRead = false order by sentDate desc")
    List<ChatMsg> getMyUnreadChatMsgs(@Param("currUserId") int currUserId);

    @Query("FROM ChatMsg where (fromId = :currUserId AND toId = :profileId) OR (fromId = :profileId AND toId = :currUserId) " +
            "order by sentDate asc")
    List<ChatMsg> getChat(@Param("currUserId") int currUserId, @Param("profileId") int profileId);

    @Query("select count(id) FROM ChatMsg where toId = :currUserId and isRead = false")
    long getNumUnreadChatMsgs(@Param("currUserId") int currUserId);

    @Query("select count(id) FROM ChatMsg where toId = :toId and fromId = :fromId and isRead = false")
    long getNumUnreadChatMsgs(@Param("fromId") int fromId, @Param("toId") int toId);

    long countByFromIdAndToId(@Param("fromId") int fromId, @Param("toId") int toId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ChatMsg set isRead = true where toId = :toId and fromId = :fromId")
    void markRead(@Param("fromId") int fromId, @Param("toId") int toId);
}
