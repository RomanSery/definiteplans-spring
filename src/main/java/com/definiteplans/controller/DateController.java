package com.definiteplans.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dao.UserRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.User;
import com.definiteplans.dom.enumerations.SubmitType;
import com.definiteplans.service.DateService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@Controller
public class DateController {
    private final UserService userService;
    private final DateService dateService;
    private final DefiniteDateRepository definiteDateRepository;
    private final UserRepository userRepository;

    public DateController(UserService userService, DateService dateService, DefiniteDateRepository definiteDateRepository, UserRepository userRepository) {
        this.userService = userService;
        this.dateService = dateService;
        this.definiteDateRepository = definiteDateRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/dates/propose")
    public @ResponseBody
    AjaxResponse proposeDate(@ModelAttribute("date") @Valid DefiniteDate date,  BindingResult bindingResult) {
        DateValidator.validatePropose(date, bindingResult);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        User currUser = userService.getCurrentUser();
        if(currUser != null) {
            Optional<User> datee = userRepository.findById(date.getDateeUserId());
            if(datee.isPresent()) {
                dateService.updateDate(currUser, datee.get(), SubmitType.PROPOSE_NEW_PLAN, date);
            }
        }
        return AjaxResponse.success("Date made!");
    }


    private static class DateValidator {
        private static void validatePropose(DefiniteDate obj, Errors e) {
            ValidationUtils.rejectIfEmpty(e, "doingWhat", "", "Doing what is required");
            ValidationUtils.rejectIfEmpty(e, "locationName", "", "Location is required");
            ValidationUtils.rejectIfEmpty(e, "doingWhenDate", "", "Doing when date is required");
            ValidationUtils.rejectIfEmpty(e, "doingWhenTime", "", "Doing when time is required");
            ValidationUtils.rejectIfEmpty(e, "greetingMsg", "", "Greeting is required");

            LocalDateTime date = DateService.getSpecificTime(obj);
            if (date != null && DateUtil.isInThePast(date, obj.getTimezone())) {
                ValidationUtils.rejectIfEmpty(e, "doingWhenDate", "", "Date can't be in the past.");
            }

        }
    }
}
