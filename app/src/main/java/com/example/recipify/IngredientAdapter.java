package com.example.recipify;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    public ArrayList<HashMap<String, Object>> ingredientMaps;

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

        HashMap<String, Object> ingredientHashMap = (HashMap<String,Object>)ingredientMaps.get(position);
        String ingredient = (String) ingredientHashMap.get("ingredient");

        if(ingredient.contains("[")){
            ingredient = ingredient.replaceAll("\\[", "");
        }

        if(ingredient.contains("]")){
            ingredient = ingredient.replaceAll("\\]", "");
        }

        System.out.println("ingredientList Type is: "+ingredient);
        System.out.println("ingredientList Type is: "+ingredient.getClass());

        ingredientViewholder.ingredient.setText(ingredient);
        ingredientViewholder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderListener.calender(position);
            }
        });
        String date = (String) ingredientHashMap.get("date");
        ingredientViewholder.date.setText(date);
    }

    @Override
    public int getItemCount() {
        return ingredientMaps.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredient;
        TextView date;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient_text_view);
            date = itemView.findViewById(R.id.date_text_view);

        }

    }

    public void setIngredientMaps(ArrayList<HashMap<String, Object>> ingredientMaps){
        this.ingredientMaps = ingredientMaps;
    }


}
