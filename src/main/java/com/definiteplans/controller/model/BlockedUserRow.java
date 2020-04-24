package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockedUserRow implements Serializable {
    private static final long serialVersionUID=1L;

    private final int userId;
    private final String displayName;

    public BlockedUserRow(int userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getLinkId() {
        return "unblock-link-" + userId;
    }
}
