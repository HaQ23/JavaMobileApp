package com.project.javapro.service;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface SlideService {
    @GET("api/slides/{lecture}")
    Call<List<String>> getSlides(@Path("lecture") String lecture);
    @Multipart
    @POST("/slides/upload")
    Call<Void> uploadSlide(@Part MultipartBody.Part file);


}
