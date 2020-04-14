package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

}
