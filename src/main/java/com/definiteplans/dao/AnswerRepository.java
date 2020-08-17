package com.definiteplans.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.definiteplans.dom.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    List<Answer> findByUserIdOrderByDateAnsweredDesc(Integer userId);

    long countByUserIdAndQuestionId(@Param("userId") int userId, @Param("questionId") int questionId);
}
