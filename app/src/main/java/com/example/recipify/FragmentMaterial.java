package com.example.recipify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FragmentMaterial extends Fragment {

    private static Animation open, close;
    private static Boolean isFabOpen = false;
    private static FloatingActionButton fab, fab1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_fragment_material, container, false);

        //플로팅 메뉴
        open = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.open);
        close = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.close);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);

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
                /*Intent intent = new Intent(getActivity(), Route.class);
                startActivity(intent);*/
            }
        });

        return  view;
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
            isFabOpen = true;
        }
    }

}