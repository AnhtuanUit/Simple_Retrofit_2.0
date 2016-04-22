package com.example.anhtuan_uit.simple_retrofit_20.BaseConnection;

import com.example.anhtuan_uit.simple_retrofit_20.DTO.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Webservice {
    @FormUrlEncoded
    @POST("/users")
    Call<User> login(@Field("username") String username,
                          @Field("avatar") String password);
}