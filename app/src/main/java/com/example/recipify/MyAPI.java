package com.example.recipify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyAPI {

    @POST("/Accounts")
    Call<Post> postAccounts(@Body Post body);

}

