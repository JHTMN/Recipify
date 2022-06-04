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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        Intent intent = getIntent();

        TextView recipeName = (TextView) findViewById(R.id.textView);
        TextView recipeIngre = (TextView) findViewById(R.id.textView2);
        final ListView listView = findViewById(R.id.listview);
        iv = findViewById(R.id.imageView);

        recipeName.setText(intent.getStringExtra("recipeName"));
        recipeIngre.setText(intent.getStringExtra("recipeIngre"));
        String splitDC = intent.getStringExtra("cookingDC");
        String[] result = splitDC.split(";;");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);
        listView.setAdapter(adapter);


        String imageStr = intent.getStringExtra("recipeImage");
        Glide.with(this).load(imageStr).into(iv);

    }

}
