package com.example.anhtuan_uit.simple_retrofit_20.DTO;

import com.google.gson.annotations.SerializedName;

public class User{
    @SerializedName("data")
    public Data data;
    public class Data{
        @SerializedName("_id")
        public String _id;
        @SerializedName("username")
        public String    username;
    }
}