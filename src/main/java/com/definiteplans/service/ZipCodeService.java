package com.definiteplans.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.ZipCode;


@Service
public class ZipCodeService {
    @Autowired private ZipCodeRepository zipCodeRepository;

    public ZipCode getById(String zip) {
        if(StringUtils.isBlank(zip)) {
            return null;
        }
        Optional<ZipCode> found = zipCodeRepository.findById(zip);
        return found.isPresent() ? found.get() : null;
    }

    public List<ZipCode> getZipCodesByRadius(ZipCode zip, int radius) {
//        List<ZipCode> arr = this.zipCodeDao.getZipCodesByRadius(zip, radius, bypassCache);
//        return (arr != null) ? arr : new ArrayList<>();
        return Collections.emptyList();
    }
}
