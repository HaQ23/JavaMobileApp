package com.project.javapro.service;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FileService {
    @Multipart
    @POST("api/file/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("api/file/uploadCode")
    Call<ResponseBody> uploadCode(@Part MultipartBody.Part file);

    @Multipart
    @POST("api/lecture/uploadSlide")
    Call<ResponseBody> uploadLectureImage(@Part MultipartBody.Part file, @Query("folder") String folder);
}
