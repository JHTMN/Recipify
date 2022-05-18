package com.syeon.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.syeon.ocr.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
        //OcrFragment.FragmentOcrListener {

    private ActivityMainBinding activityMainBinding;
    public final int REQUEST_CODE_PERMISSIONS = 100; //카메라 권한설정


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //뷰 바인딩(R.id.~~없이 view 사용가능)
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);


        //카메라 권한 설정
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            startMain(); //권한설정돼있다면 OCR 프래그먼트 열기
        }else {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
        }
    }

    //카메라 권한 설정
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            //권한 있다면 시작
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMain();
            } else { //권한 없을 경우
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void onOcrSuccess(ArrayList<String> textList) {
        Fragment frag = (OcrFragment) getSupportFragmentManager().findFragmentByTag("OcrFragment");
        if (frag != null) {
            getSupportFragmentManager().beginTransaction().remove(frag).commit();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void startMain(){
        activityMainBinding.OCRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OcrFragment myRefrigeratorFragment = OcrFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, myRefrigeratorFragment)
                        .commit();
            }
        });
    }


    /*
    private void startMain(){
        activityMainBinding.mainBottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.my_refrigerator){
                    MyRefrigeratorFragment myRefrigeratorFragment = MyRefrigeratorFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_container, myRefrigeratorFragment)
                            .commit();
                    return true;
                } else if(item.getItemId() == R.id.recommend) {
                    RecommendFragment recommendFragment = RecommendFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_container, recommendFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.recipes_search) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_container, RecipesSearchFragment.newInstance())
                            .commit();
                    return true;
                } else if(item.getItemId() == R.id.my_page) {
                    MyPageFragment myPageFragment = MyPageFragment.newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.main_container, myPageFragment).commit();
                    return true;
                } else {
                    return false;
                }

            }
        });
    }

     */
}