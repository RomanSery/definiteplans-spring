package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.ZipCode;

@Repository
public interface ZipCodeRepository extends JpaRepository<ZipCode, String>, SearchZipCodesRepository {


}
