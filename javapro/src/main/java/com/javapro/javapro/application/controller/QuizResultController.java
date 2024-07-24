package com.javapro.javapro.application.controller;

import com.javapro.javapro.application.dto.QuizResultDto;
import com.javapro.javapro.service.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz/results")
public class QuizResultController {
    @Autowired
    private QuizResultService quizResultService;

    @PostMapping
    public void saveQuizResult(@RequestBody QuizResultDto dto) {
        quizResultService.saveQuizResult(dto);
    }
}
