package com.example.recipify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.recipify.databinding.ActivityFragmentMaterialBinding;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentMaterial extends Fragment implements ItemTouchHelperListener, CalenderListener{

    private static Animation open, close;
    private static Boolean isFabOpen = false;
    private static FloatingActionButton fab, fab1, fab2;
    private static RecyclerView ingredientRecyclerView;
    private static SwipeRefreshLayout swipeRefreshLayout;

    private ActivityFragmentMaterialBinding fragmentIngredientBinding;

    private IngredientAdapter ingredientAdapter = new IngredientAdapter();

    private ArrayList<HashMap<String, Object>> ingredientMaps = new ArrayList<>();

    private ItemTouchHelperCallback itemTouchHelperCallback;
    private MaterialDatePicker datePicker;
    private SimpleDateFormat simpleDateFormat;

    public FragmentMaterial() {
        // Required empty public constructor
    }

     public static FragmentMaterial newInstance() {
        FragmentMaterial fragment = new FragmentMaterial();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                getActivity().onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this,
                onBackPressedCallback);

        itemTouchHelperCallback = new ItemTouchHelperCallback(this);
        simpleDateFormat = new SimpleDateFormat("yy/MM/dd");


//
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://2ea8-203-230-13-2.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MyingreApi myingreApi = retrofit.create(MyingreApi.class);

        Call<List<Myingre>> call = myingreApi.myingre();

        call.enqueue(new Callback<List<Myingre>>() {
            @Override
            public void onResponse(Call<List<Myingre>> call, Response<List<Myingre>> response) {

                ingredientMaps.clear();

                List<Myingre> resource = response.body();

                ArrayList<String> ingredientList = new ArrayList<> ();

                for(Myingre re : resource) {
                    ingredientList.add(re.myingre());
                }

                String[] ingredientList2 = ingredientList.toString().split(", ");

                for(String oneIngredientText: ingredientList2) {
                    HashMap<String, Object> ingredientHashMap = new HashMap<>();
                    ingredientHashMap.put("ingredient", oneIngredientText);
                    ingredientHashMap.put("date", "00/00/00");
                    ingredientMaps.add(ingredientHashMap);
                }

                //adapter에 Map set
                ingredientAdapter.setIngredientMaps(ingredientMaps);
                //ingredientAdapter.setCalenderListener(this::calender);


                //recyclerView, Adapter
                ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                ingredientRecyclerView.setAdapter(ingredientAdapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
                itemTouchHelper.attachToRecyclerView(ingredientRecyclerView);



            }

            @Override
            public void onFailure(Call<List<Myingre>> call, Throwable t) {

            }
        });

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //fragmentIngredientBinding = ActivityFragmentMaterialBinding.inflate(inflater, container, false);

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_fragment_material, container, false);


        //당겨서 새로고침
//        swipeRefreshLayout = (SwipeRefreshLayout) fragmentIngredientBinding.refreshLayout;
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(false);
//
//            }
//        });

        //플로팅 메뉴
        open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.open);
        close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.close);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        ingredientRecyclerView  = view.findViewById(R.id.ingredient_recycler_view);


        fab.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                anim();
            }
        });
        fab1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                anim();
                Intent intent = new Intent(getActivity(), GoogleLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                anim();
                Intent intent = new Intent(getActivity(), GoogleLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        return view;
    }

    //플로팅메뉴 애니메이션
    public void anim() {

        if (isFabOpen) {
            fab1.startAnimation(close);
            fab1.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(open);
            fab1.setClickable(true);
            fab2.startAnimation(open);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();



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

                //map date 변경
                ingredientMaps.get(position).put("date", date);

                ingredientAdapter.notifyDataSetChanged();

                Toast.makeText(getContext(), "selection " + date, Toast.LENGTH_SHORT).show();
            }
        });
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

