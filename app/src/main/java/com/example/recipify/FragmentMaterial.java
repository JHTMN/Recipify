package com.example.recipify;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FragmentMaterial extends Fragment implements ItemTouchHelperListener, CalenderListener, Serializable {

    private static Animation open, close;
    private static Boolean isFabOpen = false;
    private static FloatingActionButton fab, fab1, fab2;
    private static RecyclerView ingredientRecyclerView;
    private ArrayList<String> deleteList = new ArrayList<> ();
    private SwipeRefreshLayout swipeRefreshLayout;

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

        // ProgressDialog 생성
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("내 재료를 가져오고 있어요!");
        dialog.show();

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
                .baseUrl("https://6197-61-34-253-244.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        MyingreApi myingreApi = retrofit.create(MyingreApi.class);

        Call<List<Myingre>> call = myingreApi.myingre();

        call.enqueue(new Callback<List<Myingre>>() {
            @Override
            public void onResponse(Call<List<Myingre>> call, Response<List<Myingre>> response) {

                dialog.dismiss();

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
                    deleteList.add(oneIngredientText);
                }

                ingredientAdapter.notifyDataSetChanged();

                //adapter에 Map set
                ingredientAdapter.setIngredientMaps(ingredientMaps);
                //ingredientAdapter.setCalenderListener(this::calender);

                ingredientAdapter.notifyDataSetChanged();

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


        //플로팅 메뉴
        open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.open);
        close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.close);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        ingredientRecyclerView  = view.findViewById(R.id.ingredient_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ingredientAdapter.ingredientMaps.clear();

                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl("https://6197-61-34-253-244.jp.ngrok.io")
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
                            deleteList.add(oneIngredientText);
                        }

                        ingredientAdapter.notifyDataSetChanged();

                        //adapter에 Map set
                        ingredientAdapter.setIngredientMaps(ingredientMaps);
                        //ingredientAdapter.setCalenderListener(this::calender);

                        ingredientAdapter.notifyDataSetChanged();

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


                ingredientAdapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
            }
        });


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

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(FragmentMaterial.this).commit();
                FragmentMaterial.this.onDestroy();
            }
        });
        fab2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                anim();
                Intent intent = new Intent(getActivity(), Addingre.class);
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
            fab2.startAnimation(close);
            fab2.setClickable(false);
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
        ingredientAdapter.setCalenderListener(this::calender);

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
    public boolean onItemMove(int from_position, int to_position)
    {
        return false;
    }

    @Override
    public void onItemSwipe(int position) {

        HashMap<String, Object> ingredientHashMap = (HashMap<String,Object>)ingredientMaps.get(position);
        String ingredient = (String) ingredientHashMap.get("ingredient");

//        ingredientAdapter.notifyItemRemoved(position);

        if(ingredient.contains("[")){
            ingredient = ingredient.replaceAll("\\[", "");
        }

        if(ingredient.contains("]")){
            ingredient = ingredient.replaceAll("\\]", "");
        }

        //여기서 ingredient가 삭제된 식재료 값
        Toast.makeText(getContext(), ingredient+"를 삭제하였습니다.", Toast.LENGTH_SHORT).show();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://6197-61-34-253-244.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        String inputid = "asdf";

        DeleteingreApi deleteingreApi = retrofit.create(DeleteingreApi.class);
        RequestBody userID = RequestBody.create(MediaType.parse("text.plain"), inputid);
        RequestBody deleteingre = RequestBody.create(MediaType.parse("text.plain"), ingredient);

        Call<List<Deleteingrepost>> call = deleteingreApi.Deleteingre(userID, deleteingre);

        call.enqueue(new Callback<List<Deleteingrepost>>() {
            @Override
            public void onResponse(Call<List<Deleteingrepost>> call, Response<List<Deleteingrepost>> response) {

            }

            @Override
            public void onFailure(Call<List<Deleteingrepost>> call, Throwable t) {

            }
        });

//        Intent intent = new Intent(getActivity().getApplication(), NaviBar.class);
//        startActivity(intent);


    }

}

