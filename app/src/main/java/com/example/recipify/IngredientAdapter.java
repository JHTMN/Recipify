package com.example.recipify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<String> ingredientList;

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_view,parent,false);
        IngredientViewHolder ingredientViewHolder = new IngredientViewHolder(view);
        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewholder, int position) {
        String ingredients = ingredientList.get(position);
        ingredientViewholder.ingredientText.setText(ingredients);

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

    public void setIngredientList(ArrayList<String> ingredientList){
        this.ingredientList = ingredientList;
    }

}
