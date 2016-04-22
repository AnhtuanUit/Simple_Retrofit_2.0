package com.example.anhtuan_uit.simple_retrofit_20.DTO;

import com.google.gson.annotations.SerializedName;

public class Files {
    @SerializedName("data")
    public Data data;
    public class Data{
        @SerializedName("link")
        public String link;
    }
}