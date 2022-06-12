package com.example.recipify;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import android.view.View;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class signup extends AppCompatActivity {

    TextView back;
    EditText name, id, pw, pw2;
    Button pwcheck, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Retrofit 객체 생성
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://2ea8-203-230-13-2.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MyAPI myAPI = retrofit.create(MyAPI.class);

        //뒤로 가기 버튼
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        //기입 항목
        name = findViewById(R.id.signName);
        id = findViewById(R.id.signID);
        pw = findViewById(R.id.signPW);
        pw2 = findViewById(R.id.signPW2);

        //비밀번호 확인 버튼
        pwcheck = findViewById(R.id.pwcheckbutton);
        pwcheck.setOnClickListener(v -> {
            if (pw.getText().toString().equals(pw2.getText().toString())) {
                pwcheck.setText("일치");
            } else {
                Toast.makeText(signup.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
            }
        });

        //회원가입 완료 버튼
        submit = findViewById(R.id.signupbutton);


        submit.setOnClickListener(v -> {

            String inputname = name.getText().toString();
            String inputid = id.getText().toString();
            String inputpassword = pw.getText().toString();

            Post post = new Post(
                inputname,
                inputid,
                inputpassword
            );
            Call<Post> call = myAPI.postAccounts(post);

            call.enqueue(new Callback<Post>() {

                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if(response.isSuccessful()){
                        Intent intent = new Intent( signup.this, Login.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {

                }


            });

        });
    }


}