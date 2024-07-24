package com.javapro.javapro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDto  {

    private String name;
    private String surname;
    private String albumNumber;
    private Long quizId;
    private int correctAnswers;
    private int totalQuestions;
}
