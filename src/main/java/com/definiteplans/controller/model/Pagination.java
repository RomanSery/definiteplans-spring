package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination implements Serializable {
    private static final long serialVersionUID=1L;

    private final Integer prevPage;
    private final Integer currPage;
    private final Integer nextPage;
    private final boolean showPrev;
    private final boolean showNext;
    private final Integer maxPages;

    public Pagination(Integer prevPage, Integer currPage, Integer nextPage, boolean showPrev, boolean showNext, Integer maxPages) {
        this.prevPage = prevPage;
        this.currPage = currPage;
        this.nextPage = nextPage;
        this.showPrev = showPrev;
        this.showNext = showNext;
        this.maxPages = maxPages;
    }
}
