package com.syeon.ocr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class IngredientAddAdapter extends RecyclerView.Adapter<IngredientAddAdapter.IngredientAddViewHolder> {

    private ArrayList<String> ingredientList;

    @NonNull
    @Override
    public IngredientAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_add_list_view,parent,false);
        IngredientAddViewHolder ingredientAddViewHolder = new IngredientAddViewHolder(view);
        return ingredientAddViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAddViewHolder ingredientAddViewholder, int position) {
        String ingredients = ingredientList.get(position);
        ingredientAddViewholder.ingredientText.setText(ingredients);
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class IngredientAddViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientText;

        public IngredientAddViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_add_text_view);
        }
    }

    public void setIngredientList(ArrayList<String> ingredientList){
        this.ingredientList = ingredientList;
    }


}
