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

    private ArrayList<HashMap<String, Object>> ingredientMaps;

    @NonNull
    @Override
    public IngredientAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_add_list_view,parent,false);
        IngredientAddViewHolder ingredientAddViewHolder = new IngredientAddViewHolder(view);
        return ingredientAddViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAddViewHolder ingredientAddViewholder, int position) {
        HashMap<String, Object> ingredientHashMap = (HashMap<String,Object>)ingredientMaps.get(position);
        String ingredient = (String) ingredientHashMap.get("ingredient");
        ingredientAddViewholder.ingredientText.setText(ingredient);

    }

    @Override
    public int getItemCount() {
        return ingredientMaps.size();
    }

    public class IngredientAddViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientText;

        public IngredientAddViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_text);

        }
    }

    public void setIngredientMaps(ArrayList<HashMap<String, Object>> ingredientMaps){
        this.ingredientMaps = ingredientMaps;
    }


}
