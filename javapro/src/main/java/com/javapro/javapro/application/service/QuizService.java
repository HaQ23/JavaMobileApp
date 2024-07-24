package com.javapro.javapro.application.service;

import com.javapro.javapro.application.assembler.QuizAssembler;
import com.javapro.javapro.application.dto.QuizDto;
import com.javapro.javapro.domain.model.Quiz;
import com.javapro.javapro.domain.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizAssembler quizAssembler;

    public List<QuizDto> getAll() {
        return quizRepository
                .findAll()
                .stream()
                .map(quizAssembler::assemble)
                .collect(Collectors.toList());
    }

    public QuizDto getById(Long id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if(quiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return quizAssembler.assemble(quiz);
    }

    public QuizDto create(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quizAssembler.update(quizDto, quiz);

        quizRepository.save(quiz);

        return quizAssembler.assemble(quiz);
    }

    public QuizDto update(Long id, QuizDto quizDto) {
        Quiz quiz = quizRepository.findById(id).orElse(null);

        if(quiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        quizAssembler.update(quizDto, quiz);
        quizRepository.save(quiz);
        return quizAssembler.assemble(quiz);
    }

    public void delete(Long id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if(quiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        quizRepository.delete(quiz);
    }

}
