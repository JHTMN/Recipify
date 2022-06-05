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
    private static final String TAG = "MainActivity";

    private ActivityMainBinding activityMainBinding;
    public final int REQUEST_CODE_PERMISSIONS = 100; //카메라 권한설정

    Context context;
    public static NoteDatabase noteDatabase = null;


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

/*        activityMainBinding.saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                saveToDo();

                Toast.makeText(getApplicationContext(),"추가되었습니다.",Toast.LENGTH_SHORT).show();

            }
        });
        openDatabase();*/

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
                ocrFragment.setOcrFragmentListener(MainActivity.this::ocrSuccess);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ocrFragment, "OcrFragment")
                        .commit();
            } else { //식재료 값이 있을 경우 ingredientFragment 열기
                activityMainBinding.noteView.setVisibility(View.VISIBLE);
                activityMainBinding.OCRBtn.setVisibility(View.VISIBLE);
                IngredientFragment ingredientFragment = IngredientFragment.newInstance(ingredientList);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ingredientFragment, "OcrFragment")
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
                activityMainBinding.noteView.setVisibility(GONE);
                activityMainBinding.OCRBtn.setVisibility(GONE);
                OcrFragment ocrFragment = OcrFragment.newInstance();
                ocrFragment.setOcrFragmentListener(MainActivity.this::ocrSuccess);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, ocrFragment, "OcrFragment")
                        .commit();
            }
        });
    }

    private void saveToDo(){

        //EditText에 적힌 글을 가져오기
        String todo = activityMainBinding.inputToDo.getText().toString();

        //테이블에 값을 추가하는 sql구문 insert...
        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO) values (" +
                "'" + todo + "')";

        //sql문 실행
        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sqlSave);

        //저장과 동시에 EditText 안의 글 초기화
        activityMainBinding.inputToDo.setText("");
    }


    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }


}