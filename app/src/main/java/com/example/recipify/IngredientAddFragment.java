package com.example.recipify;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.recipify.databinding.FragmentIngredientAddBinding;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IngredientAddFragment<bundle> extends Fragment {

    private FragmentIngredientAddBinding fragmentIngredientAddBinding;

    private IngredientAddAdapter ingredientAddAdapter = new IngredientAddAdapter();

    String inputid = "asdf";

    private ArrayList<String> ingredientList;

    public IngredientAddFragment() {
        // Required empty public constructor
    }

    public static IngredientAddFragment newInstance(ArrayList<String> ingredientList) {
        IngredientAddFragment fragment = new IngredientAddFragment();
        Bundle args = new Bundle();
        args.putSerializable("ingredientList", ingredientList); //OCR에서 받아온 ingredientList
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null)
//        {
//            inputid = getArguments().getString("numid");
//            System.out.println(inputid);
//        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentIngredientAddBinding = FragmentIngredientAddBinding
                .inflate(inflater, container, false);
        return fragmentIngredientAddBinding.getRoot();



    }


    @Override
    public void onStart() {
        super.onStart();

        //recyclerView, Adapter
        ingredientList = (ArrayList) getArguments().getSerializable("ingredientList");

        ingredientAddAdapter.setIngredientList(ingredientList);

        fragmentIngredientAddBinding.ingredientAddRecyclerView.setAdapter(ingredientAddAdapter);
        fragmentIngredientAddBinding.ingredientAddRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        onSaveBtn();
    }

//new View.OnClickListener()
    private void onSaveBtn() {
        fragmentIngredientAddBinding.saveBtn.setOnClickListener(v -> {

            System.out.println(inputid);
            String inputingre = ingredientList.toString();

            System.out.println(ingredientList.toString());

            // Retrofit 객체 생성
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://6197-61-34-253-244.jp.ngrok.io")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();


            InsertingerApi insertingerApi = retrofit.create(InsertingerApi.class);
            RequestBody userID = RequestBody.create(MediaType.parse("text.plain"), inputid);
            RequestBody myingre = RequestBody.create(MediaType.parse("text.plain"), inputingre);

            Call<List<Insertingre>> call = insertingerApi.Insertingre(userID, myingre);

            call.enqueue(new Callback<List<Insertingre>>() {
                @Override
                public void onResponse(Call<List<Insertingre>> call, Response<List<Insertingre>> response) {
//                    System.out.println(response);

                }

                @Override
                public void onFailure(Call<List<Insertingre>> call, Throwable t) {

                }
            });

            Intent intent = new Intent(getActivity(), NaviBar.class);
            startActivity(intent);
//            @Override
//            public void onClick(View view) {
//                FragmentMaterial ingredientFragment = FragmentMaterial.newInstance();
//                FragmentManager fragmentManager = getParentFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.main_container, ingredientFragment)
//                        .commit();
//            }
        });
    }


}