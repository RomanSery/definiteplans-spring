package com.definiteplans.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;

@Service
public class DefiniteDateService {
    private final DefiniteDateRepository definiteDateRepository;
    private final ZipCodeRepository zipCodeRepository;

    public DefiniteDateService(DefiniteDateRepository definiteDateRepository, ZipCodeRepository zipCodeRepository) {
        this.definiteDateRepository = definiteDateRepository;
        this.zipCodeRepository = zipCodeRepository;
    }

    public DefiniteDate createNew(User currUser) {
        Optional<ZipCode> zip = zipCodeRepository.findById(currUser.getPostalCode());
        return new DefiniteDate(zip.isPresent() ? zip.get().getTimezone() : "America/New_York");
    }


    public Boolean wantsMore(User currUser, User viewing, DefiniteDate activeDate) {
        if (activeDate != null) {
            return null;
        }
        List<DefiniteDate> pastDates = definiteDateRepository.getPastDatesByOwnerAndDatee(currUser.getId(), viewing.getId());
        if (pastDates.size() > 0) {
            DefiniteDate dd = pastDates.get(0);
            if (viewing.getId() == dd.getOwnerUserId()) {
                return Boolean.valueOf(dd.isOwnerWantsMore());
            }
            return Boolean.valueOf(dd.isDateeWantsMore());
        }
        return null;
    }
}
