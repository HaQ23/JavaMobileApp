package com.javapro.javapro.application.assembler;

import com.javapro.javapro.application.dto.AnswerDto;
import com.javapro.javapro.domain.model.Answer;
import com.javapro.javapro.domain.model.Question;
import com.javapro.javapro.domain.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnswerAssembler {
    private final AnswerRepository answerRepository;
    public List<AnswerDto> assembleAll(List<Answer> answerList) {
        return answerList.stream().map(this::assemble).toList();
    }

    public AnswerDto assemble(Answer answer) {
        return AnswerDto
                .builder()
                .id(answer.getId())
                .name(answer.getName())
                .correct(answer.isCorrect())
                .build();
    }

    public List<Answer> assembleToEntity(List<AnswerDto> answerDtoList, Question question) {
        List<Answer> answers = new ArrayList<>();

        for (AnswerDto answerDto : answerDtoList) {
            Answer answer;
            if(answerDto.getId() == null) {
                answer = new Answer();
            } else {
                answer = answerRepository.findById(answerDto.getId()).orElse(new Answer());
            }
            answer.setName(answerDto.getName());
            answer.setCorrect(answerDto.isCorrect());
            answer.setQuestion(question);
            answers.add(answer);
        }
        return answers;
    }

    public Answer create(AnswerDto answerDto) {
        Answer answer = new Answer();
        answer.setId(answerDto.getId());
        answer.setName(answerDto.getName());
        answer.setCorrect(answerDto.isCorrect());
        return answer;
    }
}
