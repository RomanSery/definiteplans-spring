package com.coderdreams.definiteplans.dao;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coderdreams.definiteplans.dom.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u where u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByFbId(String fbId);
}
