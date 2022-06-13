package com.example.recipify;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class NaviBar extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentSearch fragmentSearch = new FragmentSearch();
    private FragmentMaterial fragmentMaterial = new FragmentMaterial();
    private FragmentFilter fragmentFilter = new FragmentFilter();
    private long backKeyPressedTime = 0;

    public NaviBar() {
        // Required empty public constructor
    }


    public static NaviBar newInstance() {
        NaviBar naviBar = new NaviBar();
        Bundle args = new Bundle();
        naviBar.setArguments(args);
        return naviBar;
    }

    private void setArguments(Bundle args) {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMaterial).commitAllowingStateLoss();

    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();


            switch(menuItem.getItemId())
            {
                case R.id.search_tap:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();
                    break;

                case R.id.material_tap:
                    transaction.replace(R.id.frameLayout, fragmentMaterial).commitAllowingStateLoss();
                    break;

                case R.id.filter_tap:
                    transaction.replace(R.id.frameLayout, fragmentFilter).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}

