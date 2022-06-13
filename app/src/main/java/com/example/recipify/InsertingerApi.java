package com.example.recipify;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface InsertingerApi {

    @Multipart
    @POST("/myingre")
    Call<List<Insertingre>> Insertingre(
            @Part("userID") RequestBody userID,
            @Part("myingre") RequestBody myingre
    );

}
