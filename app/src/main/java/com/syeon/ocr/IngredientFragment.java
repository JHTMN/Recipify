package com.syeon.ocr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syeon.ocr.databinding.FragmentIngredientBinding;

import java.util.ArrayList;


public class IngredientFragment extends Fragment {

    private FragmentIngredientBinding fragmentIngredientBinding;

    private ArrayList<String> textList;

    public IngredientFragment() {
        // Required empty public constructor
    }

    public static IngredientFragment newInstance() {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
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
        fragmentIngredientBinding = FragmentIngredientBinding
                .inflate(inflater, container, false);
        return fragmentIngredientBinding.getRoot();

    }

    public void setTextList(ArrayList<String> textList) {
        this.textList = textList;

        fragmentIngredientBinding.ingredientText.setText(textList.get(0));
    }


}