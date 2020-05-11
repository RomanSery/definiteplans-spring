package com.definiteplans.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.definiteplans.controller.model.AjaxResponse;

@Controller
public class ChatController {

    @PostMapping("/chat/send")
    public @ResponseBody
    AjaxResponse proposeDate(@ModelAttribute("chatMessage") @Valid String chatMessage, BindingResult bindingResult) {
        //DateController.DateValidator.validatePropose(proposal, bindingResult);
        //if (bindingResult.hasErrors()) {
          //  return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        //}

        //dateService.createDate(userService.getCurrentUser(), proposal);
        return AjaxResponse.success("Date made");
    }

}
