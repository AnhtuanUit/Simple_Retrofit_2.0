package com.example.anhtuan_uit.simple_retrofit_20.BaseConnection;

import com.example.anhtuan_uit.simple_retrofit_20.DTO.Files;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileUploadService {
    @Multipart
    @POST("/files/uploadFile")
    Call<Files> upload(@Part("description") String description,
                              @Part MultipartBody.Part file);
}