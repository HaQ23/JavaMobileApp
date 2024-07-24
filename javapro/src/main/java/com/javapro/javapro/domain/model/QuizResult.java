
package com.javapro.javapro.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String albumNumber;
    private Integer correctAnswers;
    private Integer totalQuestions;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
}