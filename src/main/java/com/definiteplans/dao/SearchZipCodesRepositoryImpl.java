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
        String sql = "SELECT * FROM zip_code WHERE (3958*3.1415926*sqrt((Latitude- :latitude)*(Latitude- :latitude) + cos(Latitude/57.29578)*cos(:latitude/57.29578)*(Longitude- :longitude)*(Longitude- :longitude))/180) <= :radius";

        Query q = entityManager.createNativeQuery(sql, ZipCode.class);
        q.setParameter("latitude", zip.getLatitude());
        q.setParameter("longitude", zip.getLongitude());
        q.setParameter("radius", radius);
        return q.getResultList();
    }
}
