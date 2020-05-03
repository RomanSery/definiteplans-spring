package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResult implements Serializable {
    private static final long serialVersionUID=1L;

    private final int userId;
    private final String profileImgSrc;
    private final String firstName;
    private final String location;
    private final int numNoShows;
    private final boolean wantsMore;
    private final boolean wantsNoMore;
    private final boolean hasActiveDate;
    private final boolean isOnline;

    public SearchResult(int userId, String profileImgSrc, String firstName, String location, int numNoShows, boolean wantsMore, boolean wantsNoMore, boolean hasActiveDate, boolean isOnline) {
        this.userId = userId;
        this.profileImgSrc = profileImgSrc;
        this.firstName = firstName;
        this.location = location;
        this.numNoShows = numNoShows;
        this.wantsMore = wantsMore;
        this.wantsNoMore = wantsNoMore;
        this.hasActiveDate = hasActiveDate;
        this.isOnline = isOnline;
    }
}
