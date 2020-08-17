package com.definiteplans.controller.model;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAnswer implements Serializable {
    private static final long serialVersionUID=1L;

    private final int userId;
    private final int qaId;
    private final String question;
    private final String answer;

    public UserAnswer(int userId, int qaId, String question, String answer) {
        this.userId = userId;
        this.qaId = qaId;
        this.question = question;
        this.answer = answer;
    }

    public String getAnswerPretty() {
        if (this.answer != null) {
            return this.answer.replace("\r\n", "<br>");
        }
        return "";
    }
}
