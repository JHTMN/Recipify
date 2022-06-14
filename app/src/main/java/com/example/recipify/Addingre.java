package com.example.recipify;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        EditText editSearch = findViewById(R.id.editSearch);

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

                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String text = editSearch.getText().toString()
                                .toLowerCase(Locale.getDefault());
                        search(text);
                    }

                    public void search(String charText){
                        allIngredient.clear();

                        if (charText.length() == 0) {
                            allIngredient.addAll(arrayList);
                        }
                        else
                        {
                            for(int i = 0; i < arrayList.size(); i++)
                            {
                                if(arrayList.get(i).toLowerCase().contains(charText))
                                {
                                    allIngredient.add(arrayList.get(i));
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

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