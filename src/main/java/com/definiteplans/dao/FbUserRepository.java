package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.FbUser;

@Repository
public interface FbUserRepository extends JpaRepository<FbUser, Integer> {

}
