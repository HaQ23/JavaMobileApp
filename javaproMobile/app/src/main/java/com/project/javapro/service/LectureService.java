package com.project.javapro.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LectureService {
    @GET("/api/lectures")
    Call<List<String>> getLectures();

    @POST("/api/lecture/createLecture")
    Call<Void> createLecture(@Query("folder") String folder);
}
