package com.definiteplans.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.definiteplans.controller.model.AjaxResponse;
import com.definiteplans.service.QuestionAnswerService;
import com.definiteplans.service.UserService;

@Controller
@RequestMapping(path="/me")
public class MyQaController {
    private final UserService userService;
    private final QuestionAnswerService questionAnswerService;

    public MyQaController(UserService userService, QuestionAnswerService questionAnswerService) {
        this.userService = userService;
        this.questionAnswerService = questionAnswerService;
    }

    @PostMapping("/add_qa")
    public @ResponseBody AjaxResponse saveQuestionAnswer(@ModelAttribute("selected-question") Integer selectedQuestion,
                                                         @ModelAttribute("questionAnswerTxt") String questionAnswerTxt,
                                                         BindingResult bindingResult) {
        ProfileValidator.validateQA(bindingResult, selectedQuestion, questionAnswerTxt);
        if (bindingResult.hasErrors()) {
            return AjaxResponse.error(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList()));
        }

        questionAnswerService.answerQuestion(selectedQuestion, questionAnswerTxt);
        return AjaxResponse.success("Saved");
    }

    @GetMapping("/delete_qa/{qaId}")
    public @ResponseBody AjaxResponse deleteMyQa(Model model, @PathVariable int qaId) {
        if(!questionAnswerService.deleteQA(qaId)) {
            return AjaxResponse.error(List.of("Invalid request"));
        }
        return AjaxResponse.success("Deleted");
    }

    @RequestMapping("/refresh-qa-list")
    public String refreshMyQaList(Model m) {
        int currUserId = userService.getCurrentUserId();
        m.addAttribute("my_qa", questionAnswerService.getUserAnswers(currUserId));
        return "edit_profile/qa :: my-answered-qa-frag";
    }

    @RequestMapping("/refresh-add-qa-form")
    public String refreshAddQaForm(Model m) {
        int currUserId = userService.getCurrentUserId();
        m.addAttribute("my_qa", questionAnswerService.getUserAnswers(currUserId));
        m.addAttribute("questions", questionAnswerService.getUnansweredQuestions(currUserId));
        return "edit_profile/qa :: add-qa-frag";
    }



    private static class ProfileValidator {
        private static void validateQA(Errors e, Integer selectedQuestion, String questionAnswerTxt) {
            if (selectedQuestion == null) {
                e.reject("missingQuestion", "Select a question.");
            }
            if (StringUtils.isBlank(questionAnswerTxt)) {
                e.reject("missingAnswer", "Enter your answer");
            }
            if (questionAnswerTxt != null && questionAnswerTxt.length() > 600) {
                e.reject("answerTooLong", "Answer too long");
            }
        }
    }
}
