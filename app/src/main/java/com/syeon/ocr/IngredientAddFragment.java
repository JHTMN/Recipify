package com.syeon.ocr;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.syeon.ocr.databinding.FragmentIngredientAddBinding;
import com.syeon.ocr.databinding.FragmentIngredientBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class IngredientAddFragment extends Fragment {

    private FragmentIngredientAddBinding fragmentIngredientAddBinding;

    private IngredientAddAdapter ingredientAddAdapter = new IngredientAddAdapter();;

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

        //recyclerView, Adapter
        ingredientList = (ArrayList) getArguments().getSerializable("ingredientList");

        ingredientAddAdapter.setIngredientList(ingredientList);

        fragmentIngredientAddBinding.ingredientAddRecyclerView.setAdapter(ingredientAddAdapter);
        fragmentIngredientAddBinding.ingredientAddRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        onSaveBtn();
    }


    private void onSaveBtn() {
        fragmentIngredientAddBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientFragment ingredientFragment = IngredientFragment.newInstance(ingredientList);
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ingredientFragment)
                        .commit();
            }
        });
    }


}