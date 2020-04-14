package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PwdUpdate implements Serializable {
    private static final long serialVersionUID=1L;

    private String currPwd;
    private String password1;
    private String password2;

    private Integer tokenId;
    private Integer tokenUserId;
    private String token;

    public PwdUpdate() {

    }

    public PwdUpdate(Integer tokenId, Integer tokenUserId, String token) {
        this.tokenId = tokenId;
        this.tokenUserId = tokenUserId;
        this.token = token;
    }
}
