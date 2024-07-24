package com.javapro.javapro.application.controller;

import com.javapro.javapro.application.dto.QuestionDto;
import com.javapro.javapro.application.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/random")
    public List<QuestionDto> getRandomQuestion() {
        return questionService.getRandomQuestions();
    }

    @GetMapping()
    public List<QuestionDto> getAll() {
        return questionService.getAll();
    }

    @GetMapping("/{id}")
    public QuestionDto getById(@PathVariable Long id) {
        return questionService.getById(id);
    }

    @PostMapping()
    public QuestionDto save(@RequestBody QuestionDto questionDto) {
        return questionService.save(questionDto);
    }

    @PutMapping("/{id}")
    public QuestionDto update(@PathVariable Long id, @RequestBody QuestionDto questionDto) {
        return questionService.update(id, questionDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }
}
