package com.example.recipify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSearch extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_fragment_search, container, false);

        final ListView listView = view.findViewById(R.id.listview);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://0cd4-203-230-13-202.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        SearchApi apiInterface = retrofit.create(SearchApi.class);

        Call<List<Search_Data>> call = apiInterface.getData();

        call.enqueue(new Callback<List<Search_Data>>() {

            @Override
            public void onResponse(Call<List<Search_Data>> call, Response<List<Search_Data>> response) {

                List<Search_Data> resource= response.body();

                ArrayList<String> array = new ArrayList<>();

                for(Search_Data re : resource){
                    array.add(re.recipeName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplication(), android.R.layout.simple_list_item_1, array);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(), recipeInfo.class);
//                        intent.putExtra("recipeName", array.get(i).recipeName();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Search_Data>> call, Throwable t) {

            }
        });

        return  view;


    }

}