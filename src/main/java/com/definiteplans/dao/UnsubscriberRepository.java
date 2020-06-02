package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.Unsubscriber;

@Repository
public interface UnsubscriberRepository extends JpaRepository<Unsubscriber, String> {

}
