package com.definiteplans.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.DefiniteDate;

@Repository
public interface DefiniteDateRepository extends JpaRepository<DefiniteDate, Integer> {


    @Query("FROM DefiniteDate where dateStatusId = 3 and ((ownerUserId = :ownerUserId and dateeUserId = :dateeUserId) or (ownerUserId = :dateeUserId and dateeUserId = :ownerUserId)) order by doingWhen desc")
    List<DefiniteDate> getPastDatesByOwnerAndDatee(@Param("ownerUserId") int ownerUserId, @Param("dateeUserId") int dateeUserId);

    @Query("FROM DefiniteDate where dateStatusId IN (1,2) and ((ownerUserId = :ownerUserId and dateeUserId = :dateeUserId) or (ownerUserId = :dateeUserId and dateeUserId = :ownerUserId)) order by createdDate desc")
    DefiniteDate getActiveDate(@Param("ownerUserId") int ownerUserId, @Param("dateeUserId") int dateeUserId);

    @Query("FROM DefiniteDate where dateStatusId not in (3,4) and (ownerUserId = :userId or dateeUserId = :userId)")
    List<DefiniteDate> getMyUpcomingDates(@Param("userId") int userId);

    @Query("FROM DefiniteDate where emailReminderSent = 0 and dateStatusId = 2")
    List<DefiniteDate> getDatesToSendReminderFor();

    @Query("FROM DefiniteDate where postDateEmailSent = 0 and dateStatusId = 2")
    List<DefiniteDate> getDatesToSendFeedbackReminderFor();
}
