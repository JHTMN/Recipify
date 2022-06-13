package com.example.recipify;

import com.google.gson.annotations.SerializedName;

public class Recommend {

    @SerializedName("userID")
    private String userID;

    @SerializedName("myingre")
    private String myingre;


    public String userID() {return userID;}
    public String myingre() {return myingre;}

}
