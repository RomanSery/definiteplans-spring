package com.definiteplans.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.definiteplans.controller.model.UserAnswer;
import com.definiteplans.dao.AnswerRepository;
import com.definiteplans.dao.QuestionRepository;
import com.definiteplans.dom.Answer;
import com.definiteplans.dom.Question;
import com.definiteplans.dom.User;
import com.definiteplans.util.DateUtil;


@Service
public class QuestionAnswerService {
    private final AnswerRepository answerRepository;
    private final UserService userService;

    private final List<Question> questionCache;

    public QuestionAnswerService(AnswerRepository answerRepository, UserService userService, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.userService = userService;
        questionCache = questionRepository.findAll();
    }

    public Question findQuestionById(int id) {
        return questionCache.stream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }

    public List<Question> getUnansweredQuestions(int userId) {

        Set<Integer> answeredQuestionIds = answerRepository.findByUserIdOrderByDateAnsweredDesc(userId)
                .stream().map(Answer::getQuestionId).collect(Collectors.toSet());

        return questionCache.stream().filter(q -> !answeredQuestionIds.contains(q.getId())).collect(Collectors.toList());
    }

    public void answerQuestion(Integer selectedQuestion, String questionAnswerTxt) {
        if(selectedQuestion == null || StringUtils.isBlank(questionAnswerTxt)) {
            return;
        }

        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return;
        }

        Question q = findQuestionById(selectedQuestion);
        if(q == null) {
            return;
        }

        if(answerRepository.countByUserIdAndQuestionId(currUser.getId(), q.getId()) > 0) {
            return;
        }

        Answer answer = new Answer();
        answer.setAnswerTxt(questionAnswerTxt);
        answer.setQuestionId(q.getId());
        answer.setQuestionTxt(q.getQuestionText());
        answer.setDateAnswered(DateUtil.now());
        answer.setUserId(currUser.getId());
        answerRepository.save(answer);
    }

    public boolean deleteQA(int qaId) {
        User currUser = userService.getCurrentUser();
        if(currUser == null) {
            return false;
        }

        Optional<Answer> found = answerRepository.findById(qaId);
        if(found.isEmpty()) {
           return false;
        }

        Answer answer = found.get();
        if(!Objects.equals(answer.getUserId(), currUser.getId())) {
            return false;
        }

        answerRepository.delete(answer);
        return true;
    }

    public List<UserAnswer> getUserAnswers(int userId) {
        List<UserAnswer> toReturn = new ArrayList<>();

        List<Answer> answers = answerRepository.findByUserIdOrderByDateAnsweredDesc(userId);
        for(Answer answer : answers) {
            toReturn.add(new UserAnswer(userId, answer.getId(), answer.getQuestionTxt(), answer.getAnswerTxt()));
        }

        return toReturn;
    }
}
