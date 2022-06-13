package com.example.recipify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Deleteingre extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleteingre);

        ListView listview = (ListView) findViewById(R.id.listview);
        ArrayList<String> arrayIngre = new ArrayList<>();

        String[] deleteList = (String[]) getIntent().getSerializableExtra("deleteList");
        System.out.println(deleteList);
        for(String oneIngredientText: deleteList) {
            arrayIngre.add(oneIngredientText);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Deleteingre.this, android.R.layout.simple_list_item_1, arrayIngre);
        listview.setAdapter(adapter);
    }
}