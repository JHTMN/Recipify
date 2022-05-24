package com.example.recipify;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Login extends AppCompatActivity {
    private Button signin, loginbutton;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //회원가입 버튼
        signin = findViewById(R.id.signin);
        loginbutton = findViewById(R.id.loginbutton);

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Login.this, signup.class);
                startActivity(intent);
            }

        });


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Login.this, NaviBar.class);
                startActivity(intent);
            }

        });
    }
}