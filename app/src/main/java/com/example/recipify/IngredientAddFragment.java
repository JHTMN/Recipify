package com.example.recipify;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.recipify.databinding.FragmentIngredientAddBinding;

import java.util.ArrayList;


public class IngredientAddFragment extends Fragment {

    private FragmentIngredientAddBinding fragmentIngredientAddBinding;

    private ArrayList<String> textList;

    public IngredientAddFragment() {
        // Required empty public constructor
    }

    public static IngredientAddFragment newInstance(ArrayList<String> textList) {
        IngredientAddFragment fragment = new IngredientAddFragment();
        Bundle args = new Bundle();
        args.putSerializable("textList", textList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
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
        textList = (ArrayList) getArguments().getSerializable("textList");
        IngredientAddAdapter ingredientAddAdapter = new IngredientAddAdapter();
        ingredientAddAdapter.setIngredientList(textList);
        fragmentIngredientAddBinding.ingredientRecyclerView.setAdapter(ingredientAddAdapter);
        fragmentIngredientAddBinding.ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        startSaveBtn();
    }

    private void startSaveBtn() {
        fragmentIngredientAddBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NaviBar.class);
                startActivity(intent);

                //FragmentMaterial로 이동
            }
        });
    }


}