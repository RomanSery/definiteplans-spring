package com.definiteplans.controller.model;


import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrUserInfo implements Serializable {
    private static final long serialVersionUID=1L;

    private String currUserDisplayName;
    private String currUserImg;
    private int currUserId;

    private boolean isProfileComplete;
    private List<String> missingFields;

    public CurrUserInfo() {

    }

}
