package com.syeon.ocr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>
            implements ItemTouchHelperListener{

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

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        ingredientList.remove(position);
        notifyItemRemoved(position);
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientText;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_text_view);

        }
    }

    public void setIngredientList(ArrayList<String> ingredientList){
        this.ingredientList = ingredientList;
    }

}
