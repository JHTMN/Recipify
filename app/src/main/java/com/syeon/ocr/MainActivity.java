package com.syeon.ocr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
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

    public void ocrSuccess(ArrayList<String> textList) {
        Fragment frag = (OcrFragment) getSupportFragmentManager().findFragmentByTag("OcrFragment");
        if (frag != null) {
            if(textList == null) { //식재료 값이 없으면 다시 인식
                Toast.makeText(getApplicationContext(), "영수증을 다시 인식시켜주세요", Toast.LENGTH_SHORT).show();
                getSupportFragmentManager().beginTransaction().remove(frag).commit();
                activityMainBinding.OCRBtn.setVisibility(View.GONE);
                OcrFragment ocrFragment = OcrFragment.newInstance();
                ocrFragment.setOcrFragmentListener(MainActivity.this::ocrSuccess);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ocrFragment, "OcrFragment")
                        .commit();
            } else { //식재료 값이 있을 경우 다른 프래그먼트 열기
                IngredientAddFragment ingredientAddFragment = IngredientAddFragment.newInstance(textList);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ingredientAddFragment, "OcrFragment")
                        .commit();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void startOCRbtn(){
        activityMainBinding.OCRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.OCRBtn.setVisibility(View.GONE);
                OcrFragment ocrFragment = OcrFragment.newInstance();
                ocrFragment.setOcrFragmentListener(MainActivity.this::ocrSuccess);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ocrFragment, "OcrFragment")
                        .commit();
            }
        });
    }

    /*
    private void startIngredientBtn(){
        activityMainBinding.OCRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityMainBinding.OCRBtn.setVisibility(View.GONE);
                IngredientFragment ingredientFragment = IngredientFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ingredientFragment)
                        .commit();
            }
        });
    }*/

}