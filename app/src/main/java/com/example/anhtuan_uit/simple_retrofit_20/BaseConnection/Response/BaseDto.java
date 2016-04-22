package com.example.anhtuan_uit.simple_retrofit_20.BaseConnection.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dai7oc on 2/23/2016.
 */
public abstract class BaseDto {
    @SerializedName("statusCode")
    public String statusCode;
    @SerializedName("message")
    public String msg;
}
