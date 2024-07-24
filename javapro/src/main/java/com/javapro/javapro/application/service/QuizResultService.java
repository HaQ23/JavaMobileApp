package com.javapro.javapro.service;

import com.javapro.javapro.application.assembler.QuizResultAssembler;
import com.javapro.javapro.domain.model.Quiz;
import com.javapro.javapro.domain.model.QuizResult;
import com.javapro.javapro.application.dto.QuizResultDto;
import com.javapro.javapro.domain.repository.QuizRepository;
import com.javapro.javapro.domain.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizResultAssembler quizResultAssembler;

    public void saveQuizResult(QuizResultDto dto) {
        Quiz quiz = quizRepository.findById(dto.getQuizId()).orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
        QuizResult result = quizResultAssembler.toEntity(dto, quiz);
        quizResultRepository.save(result);
    }
}
