package com.definiteplans.controller;


import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import com.definiteplans.util.StringCleaner;

@ControllerAdvice
public class CleanStringAdvice {

    @InitBinder
    public void bindStringCleaner(WebDataBinder webDataBinder) {
        StringCleaner stringCleaner = new StringCleaner();
        webDataBinder.registerCustomEditor(String.class, stringCleaner);
    }
}
