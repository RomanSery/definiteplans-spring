package com.definiteplans.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.UserImage;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {

    UserImage findByUserIdAndTimestamp(@Param("userId") Integer userId, @Param("timestamp") String timestamp);
    List<UserImage> findByUserId(@Param("userId") Integer userId);

}
