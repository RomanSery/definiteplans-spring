package com.definiteplans.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.service.DefiniteDateService;
import com.definiteplans.service.UserService;

@Controller
@RequestMapping(path="/dates")
public class DateController {
    private final UserService userService;
    private final DefiniteDateService definiteDateService;
    private final DefiniteDateRepository definiteDateRepository;

    public DateController(UserService userService, DefiniteDateService definiteDateService, DefiniteDateRepository definiteDateRepository) {
        this.userService = userService;
        this.definiteDateService = definiteDateService;
        this.definiteDateRepository = definiteDateRepository;
    }

}
