package com.example.recipify;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {
    private Button signin, loginbutton;
    EditText id, pw;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Retrofit 객체 생성
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://075c-203-230-13-202.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        CheckApi checkApi = retrofit.create(CheckApi.class);

        //회원가입 버튼
        signin = findViewById(R.id.signin);
        loginbutton = findViewById(R.id.loginbutton);
        
        //입력
        id = findViewById(R.id.editID);
        pw = findViewById(R.id.editPassword);

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Login.this, signup.class);
                startActivity(intent);
            }

        });

        //로그인 버튼
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestBody inputid = RequestBody.create(MediaType.parse("text.plain"), id.getText().toString());
                RequestBody inputpassword = RequestBody.create(MediaType.parse("text.plain"), pw.getText().toString());

                Call<Check> call = checkApi.CheckAccounts(inputid, inputpassword);

                call.enqueue(new Callback<Check>() {

                    @Override
                    public void onResponse(Call<Check> call, Response<Check> response) {
                        if(response.isSuccessful()) {
                            Intent intent = new Intent( Login.this, NaviBar.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Check> call, Throwable t) {

//                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
//                        builder1.setTitle("경고").setMessage("아이디 및 비밀번호를 다시 확인해주세요.");
//                        AlertDialog alertDialog = builder1.create();
//                        alertDialog.show();

                    }
                });


            }

        });
    }
}