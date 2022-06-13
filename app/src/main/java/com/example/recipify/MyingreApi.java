package com.example.recipify;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyingreApi {

    @GET("/myingre")
    Call<List<Myingre>> myingre();
}
