package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.EnumValue;

@Repository
public interface EnumValueRepository extends JpaRepository<EnumValue, Integer> {

}
