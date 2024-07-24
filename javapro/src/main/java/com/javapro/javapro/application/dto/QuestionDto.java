package com.javapro.javapro.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {

    private Long id;
    private String name;
    private String fileUrl;
    private Long quizId;
    private List<AnswerDto> answerList;
}
