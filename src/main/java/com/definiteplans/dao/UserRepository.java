package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, SearchUsersRepository {

    User findByEmail(@Param("email") String email);

}
