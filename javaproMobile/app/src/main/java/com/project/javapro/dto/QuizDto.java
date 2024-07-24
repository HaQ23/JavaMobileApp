package com.project.javapro.dto;

import java.io.Serializable;
import java.util.List;

public class QuizDto implements Serializable {

    private Long id;
    private String name;
    private Integer time;
    private List<QuestionDto> questions;

    public QuizDto(String name, Integer time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return this.time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<QuestionDto> getQuestions() {
        return this.questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

}
