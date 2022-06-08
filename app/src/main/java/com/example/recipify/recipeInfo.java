package com.example.recipify;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
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
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recipify.databinding.ActivityRecipeInfoBinding;

import org.w3c.dom.Text;

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
    private ImageView iv, iv1, iv2, iv3, iv4, iv5;
    private static final String TAG = "MyTag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        TextView recipeName = (TextView) findViewById(R.id.textView);
        TextView recipeIngre = (TextView) findViewById(R.id.textView2);
        TextView name01 = (TextView) findViewById(R.id.name1);
        TextView name02 = (TextView) findViewById(R.id.name2);
        TextView name03 = (TextView) findViewById(R.id.name3);
        TextView name04 = (TextView) findViewById(R.id.name4);
        TextView name05 = (TextView) findViewById(R.id.name5);

        final ListView listView = findViewById(R.id.listview);
        iv = findViewById(R.id.imageView);
        iv1 = findViewById(R.id.url1);
        iv2 = findViewById(R.id.url2);
        iv3 = findViewById(R.id.url3);
        iv4 = findViewById(R.id.url4);
        iv5 = findViewById(R.id.url5);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://2434-203-230-13-2.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        SearchApi searchApi = retrofit.create(SearchApi.class);

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
                ArrayList<String> name1 = new ArrayList<>();
                ArrayList<String> url1 = new ArrayList<>();
                ArrayList<String> name2 = new ArrayList<>();
                ArrayList<String> url2 = new ArrayList<>();
                ArrayList<String> name3 = new ArrayList<>();
                ArrayList<String> url3 = new ArrayList<>();
                ArrayList<String> name4 = new ArrayList<>();
                ArrayList<String> url4 = new ArrayList<>();
                ArrayList<String> name5 = new ArrayList<>();
                ArrayList<String> url5 = new ArrayList<>();


                Log.d(TAG, response.body().toString());

                for(Search_Data2 re : resource) {
                    arrayName.add(re.recipeName());
                    arrayIngre.add(re.recipeInd());
                    arrayImage.add(re.recipeImage());
                    arrayDC.add(re.cookingDC());
                    //
                    name1.add(re.name1());
                    url1.add(re.url1());
                    name2.add(re.name2());
                    url2.add(re.url2());
                    name3.add(re.name3());
                    url3.add(re.url3());
                    name4.add(re.name4());
                    url4.add(re.url4());
                    name5.add(re.name5());
                    url5.add(re.url5());
                }

                    String arrayN = arrayName.get(0);
                    String arrayIn = arrayIngre.get(0);
                    String arrayIm = arrayImage.get(0);
                    String arrayD = arrayDC.get(0);
                    //
                    String name_1 = name1.get(0);
                    String url_1 = url1.get(0);
                    String name_2 = name2.get(0);
                    String url_2 = url2.get(0);
                    String name_3 = name3.get(0);
                    String url_3 = url3.get(0);
                    String name_4 = name4.get(0);
                    String url_4 = url4.get(0);
                    String name_5 = name5.get(0);
                    String url_5 = url5.get(0);

                    System.out.println(name_1);
                    System.out.println(url_1);


                    recipeName.setText(arrayN);
                    recipeIngre.setText(arrayIn);
                    name01.setText(name_1);
                    name02.setText(name_2);
                    name03.setText(name_3);
                    name04.setText(name_4);
                    name05.setText(name_5);
                    String splitDC = arrayD;
                    String[] result = splitDC.split(";;");


                    ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, result);

                    int totalHeight = 0;
                    for (int i=0; i < adapter.getCount(); i++) {
                        View listItem = adapter.getView(i, null, listView);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight();
                    }
                    ViewGroup.LayoutParams params = listView.getLayoutParams();
                    params.height = totalHeight + (listView.getDividerHeight() * adapter.getCount() - 1);
                    listView.setLayoutParams(params);

                    listView.setAdapter(adapter);


                    String imageStr = arrayIm;
                    Glide.with(getApplicationContext()).load(imageStr).into(iv);
                    String imageStr1 = url_1;
                    Glide.with(getApplicationContext()).load(imageStr1).into(iv1);
                    String imageStr2 = url_2;
                    Glide.with(getApplicationContext()).load(imageStr2).into(iv2);
                    String imageStr3 = url_3;
                    Glide.with(getApplicationContext()).load(imageStr3).into(iv3);
                    String imageStr4 = url_4;
                    Glide.with(getApplicationContext()).load(imageStr4).into(iv4);
                    String imageStr5 = url_5;
                    Glide.with(getApplicationContext()).load(imageStr5).into(iv5);

            }

            @Override
            public void onFailure(Call<List<Search_Data2>> call, Throwable t) {

            }
        });



    }

}
