package com.syeon.ocr;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.HashMap;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {


    private ArrayList<HashMap<String, Object>> ingredientList;
    private CalenderListener calenderListener;

    public void setCalenderListener(CalenderListener calenderListener) {
        this.calenderListener = calenderListener;
    }


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_view,parent,false);
        IngredientViewHolder ingredientViewHolder = new IngredientViewHolder(view);
        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewholder, @SuppressLint("RecyclerView") int position) {
        HashMap<String, Object> ingredients = ingredientList.get(position);
        ingredientViewholder.ingredientText.setText((CharSequence) ingredients);

        ingredientViewholder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderListener.calender(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }


    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientText;
        TextView date;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_text_view);
            date = itemView.findViewById(R.id.date_text_view);


        }


    }

    public void setIngredientList(ArrayList<HashMap<String, Object>> ingredientList){
        this.ingredientList = ingredientList;
    }



}
