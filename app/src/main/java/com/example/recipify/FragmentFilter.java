package com.example.recipify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.recipify.databinding.ActivityFragmentFilterBinding;
import com.example.recipify.databinding.ActivityFragmentSearchBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentFilter extends Fragment implements Serializable {

    private ActivityFragmentFilterBinding activityFragmentFilterBinding;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ProgressDialog 생성
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("레시피를 추천하고 있어요! \n조금만 기다려주세요!");
        dialog.show();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES) // 연결 타임아웃
                .readTimeout(60, TimeUnit.SECONDS) // 읽기 타임아웃
                .writeTimeout(60, TimeUnit.SECONDS) // 쓰기 타임아웃
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://5138-203-230-13-2.jp.ngrok.io")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        RecommendApi recommendApi = retrofit.create(RecommendApi.class);

        Call<List<Recommend>> call = recommendApi.getData();

        call.enqueue(new Callback<List<Recommend>>() {
            @Override
            public void onResponse(Call<List<Recommend>> call, Response<List<Recommend>> response) {

                // ProgressDialog 없애기
                dialog.dismiss();

                List<Recommend> resource = response.body();
                List<String> arrayName = new ArrayList<>();
                String result = "";

                for(Recommend re : resource){
                    result = re.myingre();
                }
                System.out.println(result);

                String[] resultArray = result.split(", ");
                for (int i=0; i < resultArray.length; i++){
                    arrayName.add(resultArray[i]);
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplication(), android.R.layout.simple_list_item_1, arrayName);
                activityFragmentFilterBinding.rlistview.setAdapter(adapter);

                activityFragmentFilterBinding.rlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(), recipeInfo.class);

                        int check_position = activityFragmentFilterBinding.rlistview.getCheckedItemPosition();
                        String vo = (String) adapterView.getAdapter().getItem(i);
                        intent.putExtra("selectName", vo);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onFailure(Call<List<Recommend>> call, Throwable t) {

            }
        });


    }



    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        activityFragmentFilterBinding = ActivityFragmentFilterBinding
                .inflate(inflater, container, false);
        return  activityFragmentFilterBinding.getRoot();
    }


}