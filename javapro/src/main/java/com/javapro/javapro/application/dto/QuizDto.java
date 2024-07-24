package com.javapro.javapro.application.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {

    private Long id;
    private String name;
    private Integer time;
    private List<QuestionDto> questions;
}
