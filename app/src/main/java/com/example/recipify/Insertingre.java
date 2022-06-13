package com.example.recipify;

import com.google.gson.annotations.SerializedName;

public class Insertingre {

    @SerializedName("userID")
    private String userID;

    @SerializedName("myingre")
    private String myingre;

    public Insertingre(String inputid, String inputingre) {
        userID = inputid;
        myingre = inputingre;
    }

}
