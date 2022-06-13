package com.example.recipify;

import com.google.gson.annotations.SerializedName;

public class Deleteingrepost {

    @SerializedName("userID")
    private String userID;

    @SerializedName("deleteingre")
    private String deleteingre;

    public Deleteingrepost(String inputid, String ingredient) {
        userID = inputid;
        deleteingre = ingredient;
    }
}
