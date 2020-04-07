package com.definiteplans.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.UserStatus;
import com.definiteplans.util.Utils;
import com.github.javafaker.Faker;

@Controller
public class AdminController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EnumValueRepository enumValueRepository;
    private final ZipCodeRepository zipCodeRepository;

    public AdminController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EnumValueRepository enumValueRepository, ZipCodeRepository zipCodeRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.enumValueRepository = enumValueRepository;
        this.zipCodeRepository = zipCodeRepository;
    }

    @GetMapping("/admin/create_accounts")
    public @ResponseBody AjaxResponse createUserAccounts() {
        String pwd = "test";
        Faker faker = new Faker();

        List<EnumValue> languages = enumValueRepository.findByType(EnumValueType.LANGUAGE.getId());
        List<EnumValue> ethnicities = enumValueRepository.findByType(EnumValueType.ETHNICITY.getId());
        List<EnumValue> smokes = enumValueRepository.findByType(EnumValueType.SMOKES.getId());
        List<EnumValue> religions = enumValueRepository.findByType(EnumValueType.RELIGION.getId());
        List<EnumValue> educations = enumValueRepository.findByType(EnumValueType.EDUCATION.getId());
        List<EnumValue> incomes = enumValueRepository.findByType(EnumValueType.INCOME.getId());
        List<EnumValue> kids = enumValueRepository.findByType(EnumValueType.KIDS.getId());
        List<EnumValue> wantsKids = enumValueRepository.findByType(EnumValueType.WANTS_KIDS.getId());
        List<EnumValue> maritalStatuses = enumValueRepository.findByType(EnumValueType.MARITAL_STATUS.getId());
        List<EnumValue> genders = enumValueRepository.findByType(EnumValueType.GENDER.getId());
        List<ZipCode> zipCodes = zipCodeRepository.findAll();
        List<EnumValue> heights = Utils.heightValues;
        List<Integer> ageValues = Utils.ageValues;

        for(int i = 0; i < 5; i++) {

            ZipCode zip = zipCodes.get(rndIndex(zipCodes));

            User u = new User();
            u.setDisplayName(faker.name().firstName());
            u.setEmail(faker.name().username()+ "_" + i + "@definite.com");
            u.setCreationDate(LocalDateTime.now());
            u.setLastModifiedDate(LocalDateTime.now());
            u.setUserStatus(UserStatus.ACTIVE.getId());
            u.setDob(getRandomDOB());
            u.setPostalCode(zip.getZip());
            u.setState(zip.getState());
            u.setCity(zip.getPrimaryCity());
            u.setHeight(heights.get(rndIndex(heights)).getId());
            u.setAgeMax(ageValues.get(rndIndex(ageValues)));
            u.setAgeMin(ageValues.get(rndIndex(ageValues)));

            u.setGender(genders.get(rndIndex(genders)).getId());
            u.setEthnicity(ethnicities.get(rndIndex(ethnicities)).getId());
            u.setSmokes(smokes.get(rndIndex(smokes)).getId());
            u.setEducation(educations.get(rndIndex(educations)).getId());
            u.setMaritalStatus(maritalStatuses.get(rndIndex(maritalStatuses)).getId());
            u.setWantsKids(wantsKids.get(rndIndex(wantsKids)).getId());
            u.setKids(kids.get(rndIndex(kids)).getId());
            u.setIncome(incomes.get(rndIndex(incomes)).getId());
            u.setReligion(religions.get(rndIndex(religions)).getId());
            u.setLanguages(String.valueOf(languages.get(rndIndex(languages)).getId()));

            u.setPassword(bCryptPasswordEncoder.encode(pwd));
            userRepository.save(u);

        }

        return new AjaxResponse("OK", "created");
    }


    private int rndIndex(List<?> list) {
        return ThreadLocalRandom.current().nextInt(list.size()) % list.size();
    }

    private LocalDate getRandomDOB() {
        long minDay = LocalDate.of(1950, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2001, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate;
    }

}
