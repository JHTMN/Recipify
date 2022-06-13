package com.example.recipify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Addingre extends AppCompatActivity {

    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addingre);

        final ListView listView = findViewById(R.id.listview);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://5138-203-230-13-2.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        addAPI addAPI = retrofit.create(addAPI.class);

        Call<List<addPost>> call = addAPI.getData();

        call.enqueue(new Callback<List<addPost>>(){

            @Override
            public void onResponse(Call<List<addPost>> call, Response<List<addPost>> response) {

                List<addPost> resource = response.body();

                ArrayList<String> allIngredient = new ArrayList<>();

                for(addPost re : resource){
                    allIngredient.add(re.allIngredient());
                }

                arrayList = new ArrayList<>();
                arrayList.addAll(allIngredient);

                ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, allIngredient);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<addPost>> call, Throwable t) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String vo = (String)adapterView.getAdapter().getItem(i);

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("https://5138-203-230-13-2.jp.ngrok.io")
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();

                addAPI addAPI = retrofit.create(addAPI.class);

                RequestBody inputingre = RequestBody.create(MediaType.parse("text.plain"), vo);

                Call<List<addPost>> call = addAPI.addingre(inputingre);

                call.enqueue(new Callback<List<addPost>>() {
                    @Override
                    public void onResponse(Call<List<addPost>> call, Response<List<addPost>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<addPost>> call, Throwable t) {

                    }
                });

                Intent intent = new Intent(getApplicationContext(), NaviBar.class);
                startActivity(intent);
            }
        });

    }
}