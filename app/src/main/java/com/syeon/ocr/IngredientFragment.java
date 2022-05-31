package com.syeon.ocr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syeon.ocr.databinding.FragmentIngredientAddBinding;
import com.syeon.ocr.databinding.FragmentIngredientBinding;

import java.util.ArrayList;


public class IngredientFragment extends Fragment {

    FragmentIngredientBinding fragmentIngredientBinding;

    private ArrayList<String> textList;

    public IngredientFragment() {
        // Required empty public constructor
    }


    public static IngredientFragment newInstance(ArrayList<String> textList) {
        IngredientFragment fragment = new IngredientFragment();
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

        fragmentIngredientBinding = FragmentIngredientBinding.inflate(inflater, container, false);
        return fragmentIngredientBinding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        textList = (ArrayList) getArguments().getSerializable("textList");
        IngredientAdapter ingredientAdapter = new IngredientAdapter();
        ingredientAdapter.setIngredientList(textList);

        fragmentIngredientBinding.ingredientRecyclerView.setAdapter(ingredientAdapter);
        fragmentIngredientBinding.ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}