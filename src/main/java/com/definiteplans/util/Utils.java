package com.definiteplans.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.definiteplans.dao.EnumValueRepository;
import com.definiteplans.dom.EnumValue;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.EnumValueType;
import com.definiteplans.dom.enumerations.State;


public class Utils {

    public static final List<Integer> ageValues;
    public static final List<EnumValue> heightValues;

    static {
        ageValues = new ArrayList<>();
        for (int i = 18; i <= 100; i++) {
            ageValues.add(Integer.valueOf(i));
        }

        heightValues = new ArrayList<>();
        for (int f = 1; f <= 8; f++) {
            for (int i = 1; i <= 11; i++) {
                int numInches = f * 12 + i;
                heightValues.add(new EnumValue(numInches, f + "' " + i + "\""));
            }
        }
    }

    public static void addEnumValues(ModelAndView m, EnumValueRepository enumValueRepository, User currUser) {

        m.addObject("user", currUser);
        m.addObject("curr_user_id", currUser.getId());
        m.addObject("genders", enumValueRepository.findByType(EnumValueType.GENDER.getId()));
        m.addObject("states", State.values());
        m.addObject("ethnicities", enumValueRepository.findByType(EnumValueType.ETHNICITY.getId()));
        m.addObject("heights", heightValues);
        m.addObject("maritalStatuses", enumValueRepository.findByType(EnumValueType.MARITAL_STATUS.getId()));
        m.addObject("kidTypes", enumValueRepository.findByType(EnumValueType.KIDS.getId()));
        m.addObject("wantKidTypes", enumValueRepository.findByType(EnumValueType.WANTS_KIDS.getId()));
        m.addObject("languageTypes", enumValueRepository.findByType(EnumValueType.LANGUAGE.getId()));
        m.addObject("religions", enumValueRepository.findByType(EnumValueType.RELIGION.getId()));
        m.addObject("educations", enumValueRepository.findByType(EnumValueType.EDUCATION.getId()));
        m.addObject("incomeTypes", enumValueRepository.findByType(EnumValueType.INCOME.getId()));
        m.addObject("smokeTypes", enumValueRepository.findByType(EnumValueType.SMOKES.getId()));
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

//    public static Integer getFilterIntValue(IRequestParameters postData, String key) {
//        return (postData.getParameterValue(key) != null && isInt(postData.getParameterValue(key))) ? toInt(postData.getParameterValue(key).toString()) : null;
//    }
//
//    public static List<Integer> getFilterValues(List<StringValue> arr) {
//        List<Integer> arrParam = new ArrayList<>();
//        if (arr != null && arr.size() > 0) {
//            for (StringValue sv : arr) {
//                if (!isInt(sv))
//                    continue;
//                arrParam.add(toInt(sv.toString()));
//            }
//        }
//        return arrParam;
//    }

    public static List<Integer> getFilterValues2(List<EnumValue> arr) {
        List<Integer> arrParam = new ArrayList<>();
        if (arr != null && arr.size() > 0)
            for (EnumValue sv : arr) arrParam.add(Integer.valueOf(sv.getId()));

        return arrParam;
    }


}
