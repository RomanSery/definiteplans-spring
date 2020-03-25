package com.definiteplans.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.ZipCode;

@Repository
public interface ZipCodeRepository extends JpaRepository<ZipCode, String> {

/*
    public List<ZipCode> getZipCodesByRadius(ZipCode zip, int radius, boolean bypassCache) {
        String sql = "SELECT * FROM zip_code WHERE (3958*3.1415926*sqrt((Latitude-?)*(Latitude-?) + cos(Latitude/57.29578)*cos(?/57.29578)*(Longitude-?)*(Longitude-?))/180) <= ?";
        try {
            return getListOfObjects(sql, new Object[]{Float.valueOf(zip.getLatitude()), Float.valueOf(zip.getLatitude()), Float.valueOf(zip.getLatitude()), Float.valueOf(zip.getLongitude()), Float.valueOf(zip.getLongitude()), Integer.valueOf(radius)}, new ZipCodeMapper(), "ZipCode.getZipCodesByRadius." + zip.getZip() + "." + radius, bypassCache);
        } catch (Exception e) {
            logger.error("FAILED - getByType({})", new Object[]{zip}, e);

            return null;
        }
    }
*/
}
