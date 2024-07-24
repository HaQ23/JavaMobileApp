package com.javapro.javapro.application.service;

import com.javapro.javapro.application.assembler.QuestionAssembler;
import com.javapro.javapro.application.dto.QuestionDto;
import com.javapro.javapro.domain.model.Answer;
import com.javapro.javapro.domain.model.Question;
import com.javapro.javapro.domain.repository.AnswerRepository;
import com.javapro.javapro.domain.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionAssembler questionAssembler;

    public List<QuestionDto> getAll() {
        return questionRepository
                .findAll()
                .stream()
                .map(questionAssembler::assemble)
                .collect(Collectors.toList());
    }

    public QuestionDto getById(Long id) {
        Question question = questionRepository.findById(id).orElse(null);

        if(question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return questionAssembler.assemble(question);
    }

    public List<QuestionDto> getRandomQuestions() {
        List<Question> questions = questionRepository.findRandomQuestions();
        return questions.stream().map(questionAssembler::assemble).collect(Collectors.toList());
    }

    public QuestionDto save(QuestionDto questionDto) {
        Question question = questionAssembler.create(questionDto);
        questionRepository.save(question);

//        answerRepository.saveAll(question.getAnswers());
        return questionAssembler.assemble(question);
    }
    @Transactional
    public QuestionDto update(Long id, QuestionDto questionDto) {
        Question question = questionRepository.findById(id).orElse(null);

        if(question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        questionAssembler.update(question, questionDto);
        questionRepository.save(question);

        return questionAssembler.assemble(question);
    }

    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElse(null);

        if(question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        questionRepository.delete(question);
    }
}
