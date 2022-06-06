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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentSearch extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_fragment_search, container, false);

        final ListView listView = view.findViewById(R.id.listview);
        EditText editSearch = view.findViewById(R.id.editSearch);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://d9cb-203-230-13-202.jp.ngrok.io")
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
                        String text = editSearch.getText().toString();
                        search(text);
                    }

                    public void search(String charText){
                        arrayName.clear();


                        if (charText.length() == 0) {
                            arrayName.addAll(arrayName);
                        }
                        else
                        {
                            for(int i = 0; i < arrayName.size(); i++)
                            {
                                if(arrayName.get(i).toLowerCase().contains(charText))
                                {
                                    arrayName.add(arrayName.get(i));
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

        return  view;


    }

}