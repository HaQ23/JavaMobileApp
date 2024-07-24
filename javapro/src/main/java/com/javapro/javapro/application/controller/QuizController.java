package com.javapro.javapro.application.controller;

import com.javapro.javapro.application.dto.QuizDto;
import com.javapro.javapro.application.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping()
    public List<QuizDto> getAll() {
        return quizService.getAll();
    }

    @GetMapping("/{id}")
    public QuizDto getById(@PathVariable Long id) {
        return quizService.getById(id);
    }

    @PostMapping()
    public QuizDto create(@RequestBody QuizDto quizDto) {
        return quizService.create(quizDto);
    }

    @PutMapping("/{id}")
    public QuizDto update(@PathVariable Long id, @RequestBody QuizDto quizDto) {
        return quizService.update(id, quizDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        quizService.delete(id);
    }
}
