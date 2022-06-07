package com.syeon.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<HashMap<String, Object>> ingredientMaps;

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
        ingredientViewholder.ingredientText.setText(ingredient);
        ingredientViewholder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderListener.calender(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientMaps.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        CheckBox ingredientText;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_check_box);
            date = itemView.findViewById(R.id.date_text_view);

        }

    }

    public void setIngredientMaps(ArrayList<HashMap<String, Object>> ingredientMaps){
        this.ingredientMaps = ingredientMaps;
    }


}
