package com.example.recipify;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("username")
    private String username;
    @SerializedName("identify")
    private String identify;
    @SerializedName("password")
    private String password;

    public Post(String inputname, String inputid, String inputpassword) {
        username = inputname;
        identify = inputid;
        password = inputpassword;
    }
}
