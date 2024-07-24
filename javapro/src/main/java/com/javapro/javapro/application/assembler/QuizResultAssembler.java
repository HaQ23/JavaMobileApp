package com.javapro.javapro.application.assembler;

import com.javapro.javapro.domain.model.QuizResult;
import com.javapro.javapro.application.dto.QuizResultDto;
import com.javapro.javapro.domain.model.Quiz;
import org.springframework.stereotype.Component;

@Component
public class QuizResultAssembler {
    public QuizResult toEntity(QuizResultDto dto, Quiz quiz) {
        QuizResult result = new QuizResult();
        result.setName(dto.getName());
        result.setSurname(dto.getSurname());
        result.setAlbumNumber(dto.getAlbumNumber());
        result.setCorrectAnswers(dto.getCorrectAnswers());
        result.setTotalQuestions(dto.getTotalQuestions());
        result.setQuiz(quiz);
        return result;
    }
}