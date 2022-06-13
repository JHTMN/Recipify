package com.example.recipify;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RecommendApi {

    @GET("/ingrerecommend")
    Call<List<Recommend>> getData();

}
