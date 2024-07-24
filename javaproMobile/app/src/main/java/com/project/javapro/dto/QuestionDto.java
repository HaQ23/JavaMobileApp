package com.project.javapro.dto;

import java.io.Serializable;
import java.util.List;
public class QuestionDto implements Serializable {

    private Long id;
    private String name;
    private String fileUrl;
    private Long quizId;
    private List<AnswerDto> answerList;


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String imageUrl) {
        this.fileUrl = imageUrl;
    }

    public List<AnswerDto> getAnswerList() {
        return this.answerList;
    }

    public void setAnswerList(List<AnswerDto> answerList) {
        this.answerList = answerList;
    }

    public Long getQuizId() {
        return this.quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
