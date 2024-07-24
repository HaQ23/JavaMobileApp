package com.project.javapro.service;

import com.project.javapro.dto.QuizDto;
import com.project.javapro.dto.QuizResultDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QuizService {
    @GET("/api/quiz")
    Call<List<QuizDto>> getQuizzes();

    @GET("/api/quiz/{id}")
    Call<QuizDto> getQuizById(@Path("id") Long id);

    @POST("/api/quiz")
    Call<Void> createQuiz(@Body QuizDto quizDto);

    @PUT("/api/quiz/{id}")
    Call<Void> updateQuiz(@Path("id") Long quizId, @Body QuizDto quizDto);

    @DELETE("/api/quiz/{id}")
    Call<Void> deleteQuiz(@Path("id") Long quizId);
    @POST("/api/quiz/results")
    Call<Void> sendQuizResult(@Body QuizResultDto quizResultDto);
}