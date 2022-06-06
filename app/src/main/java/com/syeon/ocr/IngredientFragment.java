package com.syeon.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.syeon.ocr.databinding.FragmentIngredientBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class IngredientFragment extends Fragment implements ItemTouchHelperListener, CalenderListener{
    private static final String TAG = "IngredientFragment";
    private FragmentIngredientBinding fragmentIngredientBinding;
    private NoteAdapter ingredientAdapter = new NoteAdapter();;

    Context context;
    private SwipeRefreshLayout swipeRefreshLayout;


    private ArrayList<Note> ingredientMaps = new ArrayList<>();
    private ItemTouchHelperCallback itemTouchHelperCallback;
    private MaterialDatePicker datePicker;
    private SimpleDateFormat simpleDateFormat;


    public IngredientFragment() {
        // Required empty public constructor
    }

    public static IngredientFragment newInstance() { //(ArrayList<String> ingredientList) {
        IngredientFragment fragment = new IngredientFragment();
        Bundle args = new Bundle();
//        args.putSerializable("ingredientList", ingredientList); //OCR에서 받아온 ingredientList
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
        // Inflate the layout for this fragment
        fragmentIngredientBinding = FragmentIngredientBinding
                .inflate(inflater, container, false);

        loadNoteListData();

        //당겨서 새로고침
        swipeRefreshLayout = (SwipeRefreshLayout) fragmentIngredientBinding.refreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNoteListData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return fragmentIngredientBinding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        /*
        //loadNoteListDate()로 이동
        ingredientList = (ArrayList) getArguments().getSerializable("ingredientList");
        for(String oneIngredientText: ingredientList) {
            HashMap<String, Object> ingredientHashMap = new HashMap<>();
            ingredientHashMap.put("ingredient", oneIngredientText);
            ingredientHashMap.put("date", "00/00/00");
            ingredientMaps.add(ingredientHashMap);
        }

        ingredientAdapter.setItems(ingredientMaps);
        ingredientAdapter.setItems(this::calender);*/
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(fragmentIngredientBinding.ingredientRecyclerView);

        //recyclerView, Adapter
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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onPositiveButtonClick(Object selection) {
                String date = simpleDateFormat.format(selection);

                /*//map date 변경
                ingredientList = (ArrayList) getArguments().getSerializable("ingredientList");

                for(String oneIngredientText: ingredientList) {
                    HashMap<String, Object> ingredientHashMap = new HashMap<>();
                    ingredientHashMap.put("ingredient", oneIngredientText);
                    ingredientHashMap.put("date", date);
                    ingredientMaps.add(ingredientHashMap);
                }
                //ingredientAdapter.setIngredientMaps(ingredientMaps);
                //ingredientAdapter.notifyDataSetChanged();*/

                Toast.makeText(getContext(), "selection " + date, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("Range")
    public int loadNoteListData(){

        //데이터를 가져오는 sql문 select... (id의 역순으로 정렬)
        String loadSql = "select _id, TODO from " + NoteDatabase.TABLE_NOTE + " order by _id desc";

        int recordCount = -1;
        NoteDatabase database = NoteDatabase.getInstance(context);

        if(database != null){
            //cursor를 객체화하여 rawQuery문 저장
            Cursor outCursor = database.rawQuery(loadSql);

            recordCount = outCursor.getCount();

            //_id, TODO가 담겨질 배열 생성
            ArrayList<Note> items = new ArrayList<>();

            //for문을 통해 하나하나 추가
            for(int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String todo = outCursor.getString(1);
                items.add(new Note(_id,todo));
            }
            outCursor.close();

            //어댑터에 연결 및 데이터셋 변경
            ingredientAdapter.setItems(items);
            ingredientAdapter.notifyDataSetChanged();
        }

        return recordCount;
    }

    @Override
    public boolean onItemMove(int from_position, int to_position) {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {
        Toast.makeText(getContext(), "swipe"+position, Toast.LENGTH_SHORT).show();
    }

}