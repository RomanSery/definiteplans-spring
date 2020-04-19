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
import com.definiteplans.dao.ZipCodeRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.ZipCode;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.UserStatus;
import com.definiteplans.service.EnumValueService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.Utils;
import com.github.javafaker.Faker;

@Controller
public class AdminController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EnumValueService enumValueService;
    private final ZipCodeRepository zipCodeRepository;

    public AdminController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, EnumValueService enumValueService, ZipCodeRepository zipCodeRepository) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.enumValueService = enumValueService;
        this.zipCodeRepository = zipCodeRepository;
    }

    @GetMapping("/admin/create_accounts")
    public @ResponseBody AjaxResponse createUserAccounts() {
        String pwd = "test";
        Faker faker = new Faker();

        List<EnumValue> languages = enumValueService.findByType(EnumValueType.LANGUAGE);
        List<EnumValue> ethnicities = enumValueService.findByType(EnumValueType.ETHNICITY);
        List<EnumValue> smokes = enumValueService.findByType(EnumValueType.SMOKES);
        List<EnumValue> religions = enumValueService.findByType(EnumValueType.RELIGION);
        List<EnumValue> educations = enumValueService.findByType(EnumValueType.EDUCATION);
        List<EnumValue> incomes = enumValueService.findByType(EnumValueType.INCOME);
        List<EnumValue> kids = enumValueService.findByType(EnumValueType.KIDS);
        List<EnumValue> wantsKids = enumValueService.findByType(EnumValueType.WANTS_KIDS);
        List<EnumValue> maritalStatuses = enumValueService.findByType(EnumValueType.MARITAL_STATUS);
        List<EnumValue> genders = enumValueService.findByType(EnumValueType.GENDER);
        List<ZipCode> zipCodes = zipCodeRepository.findAll();
        List<EnumValue> heights = Utils.heightValues;
        List<Integer> ageValues = Utils.ageValues;

        for(int i = 0; i < 50; i++) {

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
            userService.saveUser(u);
        }

        return AjaxResponse.success("created");
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
