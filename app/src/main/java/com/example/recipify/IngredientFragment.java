package com.example.recipify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.recipify.databinding.FragmentIngredientBinding;
import com.google.android.material.datepicker.MaterialDatePicker;



import java.util.ArrayList;


public class IngredientFragment extends Fragment implements ItemTouchHelperListener, CalenderListener{

    private CalenderListener calenderListener;

    public IngredientFragment() {

    }


    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        Toast.makeText(getContext(), "swipe"+position, Toast.LENGTH_SHORT).show();
    }

    FragmentIngredientBinding fragmentIngredientBinding;

    private ArrayList<String> textList;

    private ItemTouchHelperCallback itemTouchHelperCallback;

    private MaterialDatePicker datePicker;


    public IngredientFragment(CalenderListener calenderListener) {
        // Required empty public constructor
        this.calenderListener = calenderListener;
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
        itemTouchHelperCallback = new ItemTouchHelperCallback(this);

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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(fragmentIngredientBinding.ingredientRecyclerView);

        fragmentIngredientBinding.ingredientRecyclerView.setAdapter(ingredientAdapter);
        fragmentIngredientBinding.ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }

    @Override
    public void calender(int position) {
        datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.show(getParentFragmentManager(),"calender");
        calenderListener.calender(position);
    }

}