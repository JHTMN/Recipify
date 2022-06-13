package com.example.recipify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search_Data2 {

    @SerializedName("recipeName")
    private String recipeName;

    @SerializedName("recipeInd")
    private String recipeInd;

    @SerializedName("recipeImage")
    private String recipeImage;

    @SerializedName("cookingDC")
    private String cookingDC;
//TF_IDF
    @SerializedName("name1")
    private String name1;

    @SerializedName("url1")
    private String url1;

    @SerializedName("name2")
    private String name2;

    @SerializedName("url2")
    private String url2;

    @SerializedName("name3")
    private String name3;

    @SerializedName("url3")
    private String url3;

    @SerializedName("name4")
    private String name4;

    @SerializedName("url4")
    private String url4;

    @SerializedName("name5")
    private String name5;

    @SerializedName("url5")
    private String url5;

    public String recipeName() {return recipeName;}
    public String recipeInd() {return recipeInd;}
    public String recipeImage() {return recipeImage;}
    public String cookingDC() {return cookingDC;}
    //
    public String name1() {return name1;}
    public String url1() {return url1;}
    public String name2() {return name2;}
    public String url2() {return url2;}
    public String name3() {return name3;}
    public String url3() {return url3;}
    public String name4() {return name4;}
    public String url4() {return url4;}
    public String name5() {return name5;}
    public String url5() {return url5;}

    public Search_Data2(String inputname) {
        recipeName = inputname;
    }

}
