package com.definiteplans.dom;

import java.io.Serializable;
import java.util.List;

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

}