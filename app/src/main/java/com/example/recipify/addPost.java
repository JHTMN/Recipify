package com.example.recipify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class addPost {

    @Expose
    @SerializedName("allIngredient")
    private String allIngredient;

    public String allIngredient() {
        return allIngredient;
    }

    public addPost(String inputingre) {
        allIngredient = inputingre;
    }
}
