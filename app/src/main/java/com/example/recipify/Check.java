package com.example.recipify;

import com.google.gson.annotations.SerializedName;

public class Check {
    @SerializedName("identify")
    private String identify;
    @SerializedName("password")
    private String password;

    public Check(String inputid, String inputpassword) {
        identify = inputid;
        password = inputpassword;
    }
}
