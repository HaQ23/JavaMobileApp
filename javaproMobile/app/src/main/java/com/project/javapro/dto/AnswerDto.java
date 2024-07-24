package com.project.javapro.dto;

import java.io.Serializable;

public class AnswerDto implements Serializable {

    private Long id;
    private String name;
    private boolean correct;

    public AnswerDto(Long id, String answerText, boolean isCorrect) {
        this.id = id;
        this.name = answerText;
        this.correct = isCorrect;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCorrect() {
        return this.correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
