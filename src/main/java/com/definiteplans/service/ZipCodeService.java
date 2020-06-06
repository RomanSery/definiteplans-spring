package com.definiteplans.service;


import java.time.ZoneId;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.util.DateUtil;

@Service
public class ZipCodeService {
    private static final Logger logger = LoggerFactory.getLogger(ZipCodeService.class);

    private final ZipCodeRepository zipCodeRepository;

    public ZipCodeService(ZipCodeRepository zipCodeRepository) {
        this.zipCodeRepository = zipCodeRepository;
    }


    public ZoneId getUserTimeZone(User currUser) {
        Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
        if(zip.isPresent()) {
            return ZoneId.of(zip.get().getTimezone());
        }
        return DateUtil.defaultTimeZone;
    }
}
