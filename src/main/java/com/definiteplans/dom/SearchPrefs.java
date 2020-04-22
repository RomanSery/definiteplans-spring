package com.definiteplans.dom;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPrefs implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer ageFrom;
    private Integer ageTo;
    private Integer heightFrom;
    private Integer heightTo;
    private String state;
    private Integer distance;

    private List<Integer> ethnicties;
    private List<Integer> maritalStatuses;
    private List<Integer> kids;
    private List<Integer> wantsKids;
    private List<Integer> languages;
    private List<Integer> religions;
    private List<Integer> educations;
    private List<Integer> incomes;
    private List<Integer> smokes;
    private List<Integer> genders;


    public SearchPrefs() {
        super();
    }

    public boolean hasEthnicties() {
        return ethnicties != null && !ethnicties.isEmpty();
    }
    public boolean hasMaritalStatus() {
        return maritalStatuses != null && !maritalStatuses.isEmpty();
    }
    public boolean hasKids() {
        return kids != null && !kids.isEmpty();
    }
    public boolean hasWantsKids() {
        return wantsKids != null && !wantsKids.isEmpty();
    }
    public boolean hasLanguage() {
        return languages != null && !languages.isEmpty();
    }
    public boolean hasReligion() {
        return religions != null && !religions.isEmpty();
    }
    public boolean hasEducation() {
        return educations != null && !educations.isEmpty();
    }
    public boolean hasIncome() {
        return incomes != null && !incomes.isEmpty();
    }
    public boolean hasSmokes() {
        return smokes != null && !smokes.isEmpty();
    }
    public boolean hasGender() {
        return genders != null && !genders.isEmpty();
    }

    public boolean hasState() {
        return !StringUtils.isBlank(state) && !"0".equals(state);
    }

    public boolean hasHeightFrom() {
        return heightFrom != null && heightFrom > 0;
    }

    public boolean hasHeightTo() {
        return heightTo != null && heightTo > 0;
    }

    public boolean hasAgeFrom() {
        return ageFrom != null && ageFrom > 0;
    }

    public boolean hasAgeTo() {
        return ageTo != null && ageTo > 0;
    }
}