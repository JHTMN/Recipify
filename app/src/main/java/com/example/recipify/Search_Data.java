package com.example.recipify;

import com.google.gson.annotations.SerializedName;

public class Search_Data {

    @SerializedName("recipeName")
    private String recipeName;

    public String recipeName() {
        return recipeName;
    }

}
