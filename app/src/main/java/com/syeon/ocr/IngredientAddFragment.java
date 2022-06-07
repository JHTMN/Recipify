package com.syeon.ocr;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.syeon.ocr.databinding.FragmentIngredientAddBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class IngredientAddFragment extends Fragment {

    private FragmentIngredientAddBinding fragmentIngredientAddBinding;

    private ArrayList<HashMap<String, Object>> ingredientMaps = new ArrayList<>();
    private ArrayList<String> ingredientList;

    public static NoteDatabase noteDatabase = null;
    Context context;

    public IngredientAddFragment() {
        // Required empty public constructor
    }

    public static IngredientAddFragment newInstance(ArrayList<String> ingredientList) {
        IngredientAddFragment fragment = new IngredientAddFragment();
        Bundle args = new Bundle();
        args.putSerializable("ingredientList", ingredientList); //OCR에서 받아온 ingredientList
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        fragmentIngredientAddBinding.saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveToDo();

                IngredientFragment ingredientFragment = IngredientFragment.newInstance();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ingredientFragment)
                        .commit();
            }
        });
        openDatabase();

    }


    private void saveToDo(){

        //EditText에 적힌 글을 가져오기
        String todo = fragmentIngredientAddBinding.inputToDo.getText().toString();

        //테이블에 값을 추가하는 sql구문 insert...
        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO) values (" +
                "'" + todo + "')";

        //sql문 실행
        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sqlSave);

        //저장과 동시에 EditText 안의 글 초기화
        fragmentIngredientAddBinding.inputToDo.setText("");
    }

    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        //this --> getContext
        noteDatabase = NoteDatabase.getInstance(getContext());
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentIngredientAddBinding = FragmentIngredientAddBinding
                .inflate(inflater, container, false);
        return fragmentIngredientAddBinding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();
        ingredientList = (ArrayList) getArguments().getSerializable("ingredientList");
        for(String oneIngredientText: ingredientList) {
            HashMap<String, Object> ingredientHashMap = new HashMap<>();
            ingredientHashMap.put("ingredient", oneIngredientText);
            ingredientHashMap.put("date", "00/00/00");
            ingredientMaps.add(ingredientHashMap);
        }
    }


}