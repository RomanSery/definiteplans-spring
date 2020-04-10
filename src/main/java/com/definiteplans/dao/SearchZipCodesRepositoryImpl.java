package com.definiteplans.dao;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import com.definiteplans.dom.ZipCode;

public class SearchZipCodesRepositoryImpl implements SearchZipCodesRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<ZipCode> getZipCodesByRadius(ZipCode zip, int radius) {
        String sql = "SELECT * FROM zip_code WHERE (3958*3.1415926*sqrt((Latitude-?)*(Latitude-?) + cos(Latitude/57.29578)*cos(?/57.29578)*(Longitude-?)*(Longitude-?))/180) <= ?";

        Query q = entityManager.createNativeQuery(sql, ZipCode.class);
        return q.getResultList();

//        new Object[]{Float.valueOf(zip.getLatitude()), Float.valueOf(zip.getLatitude()),
//                Float.valueOf(zip.getLatitude()), Float.valueOf(zip.getLongitude()),
//                Float.valueOf(zip.getLongitude()), Integer.valueOf(radius)}
    }
}
