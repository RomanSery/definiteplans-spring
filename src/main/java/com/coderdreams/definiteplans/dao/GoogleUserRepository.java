package com.coderdreams.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coderdreams.definiteplans.dom.GoogleUser;

@Repository
public interface GoogleUserRepository extends JpaRepository<GoogleUser, Integer> {

}
