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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.controller.model.DateFeedback;
import com.definiteplans.controller.model.DateProposal;
import com.definiteplans.dao.DefiniteDateRepository;
import com.definiteplans.dom.DefiniteDate;
import com.definiteplans.dom.enumerations.SubmitType;
import com.definiteplans.service.DateService;
import com.definiteplans.service.UserService;
import com.definiteplans.util.DateUtil;

@Controller
public class DateController {
    private final UserService userService;
    private final DateService dateService;
    private final DefiniteDateRepository definiteDateRepository;

    public DateController(UserService userService, DateService dateService, DefiniteDateRepository definiteDateRepository) {
        this.userService = userService;
        this.dateService = dateService;
        this.definiteDateRepository = definiteDateRepository;
    }

    @PostMapping("/dates/propose")
    public @ResponseBody AjaxResponse proposeDate(@ModelAttribute("date") @Valid DateProposal proposal, BindingResult bindingResult) {
        DateValidator.validatePropose(proposal, bindingResult);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        dateService.createDate(userService.getCurrentUser(), proposal);
        return AjaxResponse.success("Date made");
    }

    @PostMapping("/dates/change")
    public @ResponseBody AjaxResponse proposeChange(@ModelAttribute("date") @Valid DateProposal proposal, BindingResult bindingResult) {
        DateValidator.validatePropose(proposal, bindingResult);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        Optional<DefiniteDate> find = definiteDateRepository.findById(proposal.getId());
        if(find.isPresent()) {
            DefiniteDate date = find.get();
            date.updateFromProposal(proposal);
            dateService.updateDate(userService.getCurrentUser(), SubmitType.PROPOSE_CHANGE, date);
        }
        return AjaxResponse.success("Date changed");
    }

    @PostMapping("/dates/accept/{dateId}")
    public @ResponseBody AjaxResponse acceptDate(@PathVariable("dateId") Integer dateId) {
        Optional<DefiniteDate> date = definiteDateRepository.findById(dateId);
        if(date.isPresent()) {
            dateService.updateDate(userService.getCurrentUser(), SubmitType.ACCEPT, date.get());
        }
        return AjaxResponse.success("Date accepted");
    }

    @PostMapping("/dates/decline/{dateId}")
    public @ResponseBody AjaxResponse declineDate(@PathVariable("dateId") Integer dateId) {
        Optional<DefiniteDate> date = definiteDateRepository.findById(dateId);
        if(date.isPresent()) {
            dateService.updateDate(userService.getCurrentUser(), SubmitType.DECLINE, date.get());
        }
        return AjaxResponse.success("Date declined");
    }


    @PostMapping("/dates/feedback")
    public @ResponseBody AjaxResponse submitFeedback(@ModelAttribute("feedback") @Valid DateFeedback feedback, BindingResult bindingResult) {
        DateValidator.validateFeedback(feedback, bindingResult);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        Optional<DefiniteDate> date = definiteDateRepository.findById(feedback.getDateId());
        if(date.isPresent()) {
            dateService.submitDateFeedback(userService.getCurrentUser(), feedback, date.get());
        }
        return AjaxResponse.success("Date declined");
    }



    private static class DateValidator {
        private static void validateFeedback(DateFeedback feedback, Errors e) {
//            ValidationUtils.rejectIfEmpty(e, "doingWhat", "", "Doing what is required");
//            ValidationUtils.rejectIfEmpty(e, "locationName", "", "Location is required");
//            ValidationUtils.rejectIfEmpty(e, "doingWhenDate", "", "Doing when date is required");
//            ValidationUtils.rejectIfEmpty(e, "doingWhenTime", "", "Doing when time is required");
//            ValidationUtils.rejectIfEmpty(e, "greetingMsg", "", "Greeting is required");
//
//            LocalDateTime date = DateService.getSpecificTime(obj);
//            if (date != null && DateUtil.isInThePast(date)) {
//                e.reject("doingWhenDate", "Date can't be in the past.");
//            }
        }

        private static void validatePropose(DateProposal obj, Errors e) {
            ValidationUtils.rejectIfEmpty(e, "doingWhat", "", "Doing what is required");
            ValidationUtils.rejectIfEmpty(e, "locationName", "", "Location is required");
            ValidationUtils.rejectIfEmpty(e, "doingWhenDate", "", "Doing when date is required");
            ValidationUtils.rejectIfEmpty(e, "doingWhenTime", "", "Doing when time is required");
            ValidationUtils.rejectIfEmpty(e, "greetingMsg", "", "Greeting is required");

            LocalDateTime date = DateService.getSpecificTime(obj);
            if (date != null && DateUtil.isInThePast(date)) {
                e.reject("doingWhenDate", "Date can't be in the past.");
            }
        }
    }
}
