package com.syeon.ocr;

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

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.type.DateTime;
import com.syeon.ocr.databinding.FragmentIngredientAddBinding;
import com.syeon.ocr.databinding.FragmentIngredientBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.SimpleTimeZone;


public class IngredientFragment extends Fragment implements ItemTouchHelperListener, CalenderListener{

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

    private ArrayList<HashMap<String,Object>> textList;

    private ItemTouchHelperCallback itemTouchHelperCallback;

    private MaterialDatePicker datePicker;
    private SimpleDateFormat simpleDateFormat;
    IngredientAdapter ingredientAdapter;


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
        simpleDateFormat = new SimpleDateFormat("yy/MM/dd");
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
        ingredientAdapter = new IngredientAdapter();
        ingredientAdapter.setIngredientList(textList);
        ingredientAdapter.setCalenderListener(this::calender);
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

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                String date = simpleDateFormat.format(selection);
                //map date 변경
                ingredientAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "selection " + date, Toast.LENGTH_SHORT).show();
            }
        });
    }

}