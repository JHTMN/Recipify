package com.example.recipify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.recipify.databinding.ActivityFragmentSearchBinding;
import com.example.recipify.databinding.FragmentIngredientAddBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSearch extends Fragment {

    private ActivityFragmentSearchBinding activityFragmentSearchBinding;

    private ArrayList<String> arrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        final ListView listView = view.findViewById(R.id.listview);
        EditText editSearch = view.findViewById(R.id.editSearch);
*/

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://2ea8-203-230-13-2.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        SearchApi apiInterface = retrofit.create(SearchApi.class);

        Call<List<Search_Data>> call = apiInterface.getData();

        call.enqueue(new Callback<List<Search_Data>>() {

            @Override
            public void onResponse(Call<List<Search_Data>> call, Response<List<Search_Data>> response) {

                List<Search_Data> resource= response.body();

                ArrayList<String> arrayID = new ArrayList<>();
                ArrayList<String> arrayName = new ArrayList<>();
                ArrayList<String> arrayIngre = new ArrayList<>();
                ArrayList<String> arrayImage = new ArrayList<>();
                ArrayList<String> arrayDC = new ArrayList<>();


                for(Search_Data re : resource){
                    arrayID.add(re.recipeID());
                    arrayName.add(re.recipeName());
                    arrayIngre.add(re.recipeInd());
                    arrayImage.add(re.recipeImage());
                    arrayDC.add(re.cookingDC());
                }
                System.out.println(arrayName);
                System.out.println(arrayIngre);
                System.out.println(arrayDC);

                arrayList = new ArrayList<>();
                arrayList.addAll(arrayName);

                System.out.println(arrayList);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplication(), android.R.layout.simple_list_item_1, arrayName);
                activityFragmentSearchBinding.listview.setAdapter(adapter);

                activityFragmentSearchBinding.editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String text = activityFragmentSearchBinding.editSearch.getText().toString()
                                .toLowerCase(Locale.getDefault());
                        search(text);
                    }

                    public void search(String charText){
                        arrayName.clear();

                        if (charText.length() == 0) {
                            arrayName.addAll(arrayList);
                        }
                        else
                        {
                            for(int i = 0; i < arrayList.size(); i++)
                            {
                                if(arrayList.get(i).toLowerCase().contains(charText))
                                {
                                    arrayName.add(arrayList.get(i));
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                activityFragmentSearchBinding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(), recipeInfo.class);
                        int check_position = activityFragmentSearchBinding.listview.getCheckedItemPosition();
                        String vo = (String)adapterView.getAdapter().getItem(i);
                        intent.putExtra("selectName", vo);

                        intent.putExtra("recipeID", arrayID.get(i));
                        intent.putExtra("recipeName", arrayName.get(i));
                        intent.putExtra("recipeIngre", arrayIngre.get(i));
                        intent.putExtra("recipeImage", arrayImage.get(i));
                        intent.putExtra("cookingDC", arrayDC.get(i));
                        startActivity(intent);
                    }
                });

            }



            @Override
            public void onFailure(Call<List<Search_Data>> call, Throwable t) {

            }
        });


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activityFragmentSearchBinding = ActivityFragmentSearchBinding
                .inflate(inflater, container, false);
        return activityFragmentSearchBinding.getRoot();

/*
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_fragment_search, container, false);
*/

        /*final ListView listView = view.findViewById(R.id.listview);
        EditText editSearch = view.findViewById(R.id.editSearch);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://cd27-203-230-13-2.jp.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        SearchApi apiInterface = retrofit.create(SearchApi.class);

        Call<List<Search_Data>> call = apiInterface.getData();

        call.enqueue(new Callback<List<Search_Data>>() {

            @Override
            public void onResponse(Call<List<Search_Data>> call, Response<List<Search_Data>> response) {

                List<Search_Data> resource= response.body();

                ArrayList<String> arrayID = new ArrayList<>();
                ArrayList<String> arrayName = new ArrayList<>();
                ArrayList<String> arrayIngre = new ArrayList<>();
                ArrayList<String> arrayImage = new ArrayList<>();
                ArrayList<String> arrayDC = new ArrayList<>();


                for(Search_Data re : resource){
                    arrayID.add(re.recipeID());
                    arrayName.add(re.recipeName());
                    arrayIngre.add(re.recipeInd());
                    arrayImage.add(re.recipeImage());
                    arrayDC.add(re.cookingDC());
                }
                System.out.println(arrayName);
                System.out.println(arrayIngre);
                System.out.println(arrayDC);

                arrayList = new ArrayList<>();
                arrayList.addAll(arrayName);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplication(), android.R.layout.simple_list_item_1, arrayName);
                listView.setAdapter(adapter);

                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String text = editSearch.getText().toString()
                                .toLowerCase(Locale.getDefault());
                        search(text);
                    }

                    public void search(String charText){
                        arrayName.clear();

                        if (charText.length() == 0) {
                            arrayName.addAll(arrayList);
                        }
                        else
                        {
                            for(int i = 0; i < arrayList.size(); i++)
                            {
                                if(arrayList.get(i).toLowerCase().contains(charText))
                                {
                                    arrayName.add(arrayList.get(i));
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(), recipeInfo.class);
                        int check_position = listView.getCheckedItemPosition();
                        String vo = (String)adapterView.getAdapter().getItem(i);
                        intent.putExtra("selectName", vo);

                        intent.putExtra("recipeID", arrayID.get(i));
                        intent.putExtra("recipeName", arrayName.get(i));
                        intent.putExtra("recipeIngre", arrayIngre.get(i));
                        intent.putExtra("recipeImage", arrayImage.get(i));
                        intent.putExtra("cookingDC", arrayDC.get(i));
                        startActivity(intent);
                    }
                });

            }



            @Override
            public void onFailure(Call<List<Search_Data>> call, Throwable t) {

            }
        });
*/


/*
        return  view;
*/


    }

}