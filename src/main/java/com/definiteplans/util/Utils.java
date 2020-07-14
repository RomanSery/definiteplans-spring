package com.definiteplans.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.State;
import com.definiteplans.service.EnumValueService;


public class Utils {

    public static final List<Integer> ageValues;
    public static final List<EnumValue> heightValues;

    static {
        ageValues = new ArrayList<>(100);
        for (int i = 18; i <= 100; i++) {
            ageValues.add(Integer.valueOf(i));
        }

        heightValues = new ArrayList<>(100);
        for (int f = 1; f <= 8; f++) {
            for (int i = 1; i <= 11; i++) {
                int numInches = f * 12 + i;
                heightValues.add(new EnumValue(numInches, f + "' " + i + '"'));
            }
        }
    }

    public static String getHeightById(int id) {
        EnumValue value = heightValues.stream().filter(h -> h.getId() == id).findFirst().orElse(null);
        return value != null ? value.getEnumValue() : "";
    }

    public static void addEnumValues(ModelAndView m, EnumValueService enumValueService, User currUser) {

        m.addObject("user", currUser);
        m.addObject("curr_user_id", currUser.getId());
        m.addObject("firebase_id", currUser.getFireBaseId());
        m.addObject("genders", enumValueService.findByType(EnumValueType.GENDER));
        m.addObject("states", State.values());
        m.addObject("ethnicities", enumValueService.findByType(EnumValueType.ETHNICITY));
        m.addObject("heights", heightValues);
        m.addObject("maritalStatuses", enumValueService.findByType(EnumValueType.MARITAL_STATUS));
        m.addObject("kidTypes", enumValueService.findByType(EnumValueType.KIDS));
        m.addObject("wantKidTypes", enumValueService.findByType(EnumValueType.WANTS_KIDS));
        m.addObject("languageTypes", enumValueService.findByType(EnumValueType.LANGUAGE));
        m.addObject("religions", enumValueService.findByType(EnumValueType.RELIGION));
        m.addObject("educations", enumValueService.findByType(EnumValueType.EDUCATION));
        m.addObject("incomeTypes", enumValueService.findByType(EnumValueType.INCOME));
        m.addObject("smokeTypes", enumValueService.findByType(EnumValueType.SMOKES));
        m.addObject("ages", ageValues);
        m.addObject("distances", List.of(10, 15, 25));
    }

    public static String getVariable(String name) {
        String result = System.getProperty(name);
        if (StringUtils.isBlank(result)) {
            result = System.getenv(name);
        }
        return result;
    }
}
