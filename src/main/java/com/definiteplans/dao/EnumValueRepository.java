package com.definiteplans.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.EnumValue;

@Repository
public interface EnumValueRepository extends JpaRepository<EnumValue, Integer> {

    List<EnumValue> findByType(@Param("type") int type);
}
