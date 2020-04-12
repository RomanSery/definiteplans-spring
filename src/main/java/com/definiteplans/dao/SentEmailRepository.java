package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.SentEmail;

@Repository
public interface SentEmailRepository extends JpaRepository<SentEmail, Integer> {

}
