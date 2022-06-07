package com.syeon.ocr;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.syeon.ocr.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OcrFragment.OcrFragmentListener {

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
            startOCRbtn(); //권한설정돼있다면 OCR 프래그먼트 열기
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
                startOCRbtn();
            } else { //권한 없을 경우
                Toast.makeText(this,
                        "Permissions not granted by the user.",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void ocrSuccess(ArrayList<String> ingredientList) {
        Fragment frag = (OcrFragment) getSupportFragmentManager().findFragmentByTag("OcrFragment");
        if (frag != null) {
            if(ingredientList == null) { //식재료 값이 없으면 다시 인식
                Toast.makeText(getApplicationContext(), "영수증을 다시 인식시켜주세요", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().remove(frag).commit();
                activityMainBinding.OCRBtn.setVisibility(GONE);
                OcrFragment ocrFragment = OcrFragment.newInstance();
                ocrFragment.setOcrFragmentListener(MainActivity.this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ocrFragment, "OcrFragment")
                        .commit();
            } else { //식재료 값이 있을 경우 ingredientFragment 열기
                activityMainBinding.OCRBtn.setVisibility(View.VISIBLE);
                IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(ingredientList);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ingredientAddFragment, "IngredientAddFragment")
                        .addToBackStack("IngredientAddFragment")
                        .commit();
            }
        }
    }

    public void ocrVisibleView() {
        activityMainBinding.OCRBtn.setVisibility(View.VISIBLE);
    }



    private void startOCRbtn(){
        activityMainBinding.OCRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.OCRBtn.setVisibility(GONE);
                OcrFragment ocrFragment = OcrFragment.newInstance();
                ocrFragment.setOcrFragmentListener(MainActivity.this);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ocrFragment, "OcrFragment")
                        .addToBackStack("OcrFragment")
                        .commit();
            }
        });
    }



}