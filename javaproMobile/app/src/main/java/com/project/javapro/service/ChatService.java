package com.project.javapro.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatService {
    @GET("/chat")
    Call<String> getChatResponse(@Query("prompt") String prompt);
}
