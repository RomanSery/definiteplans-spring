package com.definiteplans.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.UserEmail;

@Repository
public interface UserEmailRepository extends JpaRepository<UserEmail, String> {

    List<UserEmail> findByUserId(Integer userId);
    Optional<UserEmail> findByEmail(String email);
}
