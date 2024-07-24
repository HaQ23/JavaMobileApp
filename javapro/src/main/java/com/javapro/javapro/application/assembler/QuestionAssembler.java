package com.javapro.javapro.application.assembler;

import com.javapro.javapro.application.dto.QuestionDto;
import com.javapro.javapro.domain.model.Answer;
import com.javapro.javapro.domain.model.Question;
import com.javapro.javapro.domain.model.Quiz;
import com.javapro.javapro.domain.repository.AnswerRepository;
import com.javapro.javapro.domain.repository.QuestionRepository;
import com.javapro.javapro.domain.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionAssembler {
    private final AnswerAssembler answerAssembler;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public List<QuestionDto> assembleAll(List<Question> questions) {
        if(questions == null || questions.isEmpty()) {
            return null;
        }
        return questions.stream().map(this::assemble).toList();
    }

    public QuestionDto assemble(Question question) {
        return QuestionDto
                .builder()
                .id(question.getId())
                .name(question.getName())
                .fileUrl(question.getFileUrl())
                .quizId(question.getQuiz().getId())
                .answerList(answerAssembler.assembleAll(question.getAnswers()))
                .build();
    }

    public Question create(QuestionDto questionDto) {
        Quiz quiz = quizRepository.findById(questionDto.getQuizId()).orElseThrow();
        Question question = new Question();
        question.setName(questionDto.getName());
        question.setFileUrl(questionDto.getFileUrl());
        question.setQuiz(quiz);
        question.setAnswers(answerAssembler.assembleToEntity(questionDto.getAnswerList(), question));
        return question;
    }

    public Question update(Question question, QuestionDto questionDto) {
        question.setName(questionDto.getName());
        question.setFileUrl(questionDto.getFileUrl());
        List<Answer> newAnswers = answerAssembler.assembleToEntity(questionDto.getAnswerList(), question);
        answerRepository.deleteAllById(question.getAnswers().stream().map(Answer::getId).toList());
        answerRepository.flush();
        question.getAnswers().clear();
        question.getAnswers().addAll(newAnswers);
        return question;
    }
}
