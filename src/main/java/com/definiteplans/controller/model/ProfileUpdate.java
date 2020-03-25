package com.definiteplans.controller.model;


import java.io.Serializable;

import com.definiteplans.dom.User;

public class ProfileUpdate implements Serializable {
    private static final long serialVersionUID=1L;

    private String displayName;


    public ProfileUpdate(User u) {
        if(u != null) {
            this.displayName = u.getDisplayName();
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
