package com.example.recipify;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recipify.databinding.ActivityRecipeInfoBinding;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class recipeInfo extends AppCompatActivity {
    private ImageView iv;
    private static final String TAG = "MyTag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        TextView recipeName = (TextView) findViewById(R.id.textView);
        TextView recipeIngre = (TextView) findViewById(R.id.textView2);
        final ListView listView = findViewById(R.id.listview);
        iv = findViewById(R.id.imageView);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://527b-203-230-13-202.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        SearchApi searchApi = retrofit.create(SearchApi.class);
        //

        String selectName = getIntent().getStringExtra("selectName");

        System.out.println(selectName);

        RequestBody inputid = RequestBody.create(MediaType.parse("text.plain"), selectName);

        Call<List<Search_Data2>> call = searchApi.recipeName(inputid);

        call.enqueue(new Callback<List<Search_Data2>>() {
            @Override
            public void onResponse(Call<List<Search_Data2>> call, Response<List<Search_Data2>> response) {
               List<Search_Data2> resource = response.body();

                ArrayList<String> arrayName = new ArrayList<>();
                ArrayList<String> arrayIngre = new ArrayList<>();
                ArrayList<String> arrayImage = new ArrayList<>();
                ArrayList<String> arrayDC = new ArrayList<>();


                Log.d(TAG, response.body().toString());

                for(Search_Data2 re : resource) {
                    arrayName.add(re.recipeName());
                    arrayIngre.add(re.recipeInd());
                    arrayImage.add(re.recipeImage());
                    arrayDC.add(re.cookingDC());

                    String arrayN = arrayName.get(0);
                    String arrayIn = arrayIngre.get(0);
                    String arrayIm = arrayImage.get(0);
                    String arrayD = arrayDC.get(0);


                    recipeName.setText(arrayN);
                    recipeIngre.setText(arrayIn);
                    String splitDC = arrayD;
                    String[] result = splitDC.split(";;");

                    ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, result);
                    listView.setAdapter(adapter);


                    String imageStr = arrayIm;
                    Glide.with(getApplicationContext()).load(imageStr).into(iv);


                }
            }

            @Override
            public void onFailure(Call<List<Search_Data2>> call, Throwable t) {

            }
        });



        Intent intent = getIntent();







    }

}
