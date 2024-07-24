package com.project.javapro.service;

import com.project.javapro.dto.QuestionDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QuestionService {

    @POST("api/question")
    Call<Void> createQuestion(@Body QuestionDto questionDto);

    @PUT("api/question/{id}")
    Call<Void> updateQuestion(@Path("id") Long id, @Body QuestionDto questionDto);

    @DELETE("api/question/{id}")
    Call<Void> deleteQuestion(@Path("id") Long id);
}