package com.definiteplans.controller.model;


import java.io.Serializable;

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

    public SearchResult(int userId, String profileImgSrc, String firstName, String location, int numNoShows, boolean wantsMore, boolean wantsNoMore, boolean hasActiveDate) {
        this.userId = userId;
        this.profileImgSrc = profileImgSrc;
        this.firstName = firstName;
        this.location = location;
        this.numNoShows = numNoShows;
        this.wantsMore = wantsMore;
        this.wantsNoMore = wantsNoMore;
        this.hasActiveDate = hasActiveDate;
    }

    public String getProfileImgSrc() {
        return profileImgSrc;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLocation() {
        return location;
    }

    public int getNumNoShows() {
        return numNoShows;
    }

    public boolean isWantsMore() {
        return wantsMore;
    }

    public boolean isHasActiveDate() {
        return hasActiveDate;
    }

    public boolean isWantsNoMore() {
        return wantsNoMore;
    }

    public int getUserId() {
        return userId;
    }
}
