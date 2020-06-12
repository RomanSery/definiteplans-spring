package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.definiteplans.dom.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, SearchUsersRepository {

    User findByEmail(@Param("email") String email);

    User findByGoogleSubId(@Param("googleSubId") String googleSubId);
    User findByFbId(@Param("fbId") String fbId);

    @Transactional
    @Procedure("DELETE_ACCOUNT")
    void deleteAccount(@Param("user_to_delete") Integer userToDeleteId);
}
