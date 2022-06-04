package com.syeon.ocr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syeon.ocr.databinding.FragmentIngredientAddBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class IngredientAddFragment extends Fragment {

    private FragmentIngredientAddBinding fragmentIngredientAddBinding;

    private ArrayList<HashMap<String, Object>> ingredientMaps = new ArrayList<>();
    private ArrayList<String> ingredientList;

    private IngredientAddAdapter ingredientAddAdapter = new IngredientAddAdapter();



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
        ingredientList = (ArrayList) getArguments().getSerializable("ingredientList");
        for(String oneIngredientText: ingredientList) {
            HashMap<String, Object> ingredientHashMap = new HashMap<>();
            ingredientHashMap.put("ingredient", oneIngredientText);
            ingredientHashMap.put("date", "00/00/00");
            ingredientMaps.add(ingredientHashMap);
        }
        ingredientAddAdapter.setIngredientMaps(ingredientMaps);
        fragmentIngredientAddBinding.ingredientRecyclerView.setAdapter(ingredientAddAdapter);
        fragmentIngredientAddBinding.ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        startSaveBtn();
    }

    private void startSaveBtn() {
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