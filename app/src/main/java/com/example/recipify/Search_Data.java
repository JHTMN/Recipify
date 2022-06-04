package com.example.recipify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search_Data {

    @Expose
    @SerializedName("recipeID")
    private String recipeID;

    @Expose
    @SerializedName("recipeName")
    private String recipeName;

    @Expose
    @SerializedName("recipeInd")
    private String recipeInd;

    @Expose
    @SerializedName("recipeImage")
    private String recipeImage;

    @Expose
    @SerializedName("cookingDC")
    private String cookingDC;

    public String recipeID() {
        return recipeID;
    }
    public String recipeName() {
        return recipeName;
    }
    public String recipeInd() { return recipeInd; }
    public String recipeImage() { return recipeImage; }
    public String cookingDC() { return cookingDC; }

}
