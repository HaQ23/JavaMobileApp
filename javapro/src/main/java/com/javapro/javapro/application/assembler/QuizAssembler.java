package com.javapro.javapro.application.assembler;

import com.javapro.javapro.application.dto.QuizDto;
import com.javapro.javapro.domain.model.Question;
import com.javapro.javapro.domain.model.Quiz;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizAssembler {
    private final QuestionAssembler questionAssembler;
    public QuizDto assemble(Quiz quiz) {
        return QuizDto
                .builder()
                .id(quiz.getId())
                .name(quiz.getName())
                .time(quiz.getTime())
                .questions(questionAssembler.assembleAll(quiz.getQuestionList()))
                .build();
    }


    public Quiz update(QuizDto quizDto, Quiz quiz) {
        quiz.setName(quizDto.getName());
        quiz.setTime(quizDto.getTime());
        return quiz;
    }
}
