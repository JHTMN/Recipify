package com.syeon.ocr;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.syeon.ocr.databinding.FragmentOcrBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class OcrFragment extends Fragment {

    private FragmentOcrBinding fragmentOcrBinding;

    private OcrFragmentListener ocrFragmentListener;

    //카메라
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private Executor cameraExecutor;
    private ImageCapture imageCapture;

    //Functions
    private FirebaseFunctions mFunctions;

    HashMap<String, Object> ingredientMap;

    interface OcrFragmentListener{
        void ocrSuccess(ArrayList<String> textList); //인식된 식재료를 넘기기 위해
    }
    public void setOcrFragmentListener(OcrFragmentListener ocrFragmentListener){
        this.ocrFragmentListener = ocrFragmentListener;
    }

    public OcrFragment() {
    }

    public static OcrFragment newInstance() {
        OcrFragment fragment = new OcrFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        cameraExecutor = ContextCompat.getMainExecutor(requireContext());
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
                // 1.Cloud Functions 인스턴스 초기화
        mFunctions = FirebaseFunctions.getInstance();
        ingredientMap = new HashMap<String, Object>();
        try {
            readDataFromCsv("./drop_duplicates_ingre.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ingredientMap.put("떡", 0);
        ingredientMap.put("간장", 1);
        ingredientMap.put("계란", 2);
        ingredientMap.put("고사리", 3);
        ingredientMap.put("고추장", 4);
        ingredientMap.put("다진마늘", 5);
        ingredientMap.put("다진파", 6);
        ingredientMap.put("도라지", 7);
        ingredientMap.put("돼지고기", 8);
        ingredientMap.put("묵", 9);
        ingredientMap.put("미나리", 10);
        ingredientMap.put("설탕", 11);
        ingredientMap.put("소금", 12);
        ingredientMap.put("쇠고기", 13);
        ingredientMap.put("숙주", 14);
        ingredientMap.put("쌀", 15);
        ingredientMap.put("참기름", 16);
        ingredientMap.put("콩나물", 17);
        ingredientMap.put("멥쌀", 18);
        ingredientMap.put("수수", 19);
        ingredientMap.put("차조", 20);
        ingredientMap.put("찹쌀", 21);
        ingredientMap.put("콩", 22);
        ingredientMap.put("팥", 23);
        ingredientMap.put("고추", 24);
        ingredientMap.put("당근", 25);
        ingredientMap.put("당면", 26);
        ingredientMap.put("밥", 27);
        ingredientMap.put("부추", 28);
        ingredientMap.put("표고버섯", 29);
        ingredientMap.put("호박", 30);
        ingredientMap.put("후춧가루", 31);
        ingredientMap.put("고춧가루", 32);
        ingredientMap.put("깨소금", 33);
        ingredientMap.put("마늘", 34);
        ingredientMap.put("파", 35);
        ingredientMap.put("계피", 36);
        ingredientMap.put("대추", 37);
        ingredientMap.put("물엿", 38);
        ingredientMap.put("밤", 39);
        ingredientMap.put("식용유", 40);
        ingredientMap.put("잣", 41);
        ingredientMap.put("흑임자", 42);
        ingredientMap.put("감자", 43);
        ingredientMap.put("양파", 44);
        ingredientMap.put("완두콩", 45);
        ingredientMap.put("우유", 46);
        ingredientMap.put("카레가루", 47);
        ingredientMap.put("다시마", 48);
        ingredientMap.put("멸치", 49);
        ingredientMap.put("쌈장", 50);
        ingredientMap.put("오이", 51);
        ingredientMap.put("전분", 52);
        ingredientMap.put("피망", 53);
        ingredientMap.put("밀가루", 54);
        ingredientMap.put("실파", 55);
        ingredientMap.put("애호박", 56);
        ingredientMap.put("양념장", 57);
        ingredientMap.put("냉면", 58);
        ingredientMap.put("동치미국물", 59);
        ingredientMap.put("동치미무", 60);
        ingredientMap.put("배", 61);
        ingredientMap.put("쇠고기육수", 62);
        ingredientMap.put("식초", 63);
        ingredientMap.put("국수", 64);
        ingredientMap.put("겨자", 65);
        ingredientMap.put("다진생강", 66);
        ingredientMap.put("대파", 67);
        ingredientMap.put("열무", 68);
        ingredientMap.put("찹쌀가루", 69);
        ingredientMap.put("깻잎", 70);
        ingredientMap.put("상추", 71);
        ingredientMap.put("계란흰자", 72);
        ingredientMap.put("녹말", 73);
        ingredientMap.put("멸칫국물", 74);
        ingredientMap.put("새우", 75);
        ingredientMap.put("오징어", 76);
        ingredientMap.put("죽순", 77);
        ingredientMap.put("청주", 78);
        ingredientMap.put("홍합", 79);
        ingredientMap.put("김치", 80);
        ingredientMap.put("두부", 81);
        ingredientMap.put("만두피", 82);
        ingredientMap.put("육수", 83);
        ingredientMap.put("모시조개", 84);
        ingredientMap.put("북어", 85);
        ingredientMap.put("무", 86);
        ingredientMap.put("미역", 87);
        ingredientMap.put("생태", 88);
        ingredientMap.put("레몬즙", 89);
        ingredientMap.put("모듬채소", 90);
        ingredientMap.put("방울토마토", 91);
        ingredientMap.put("양송이버섯", 92);
        ingredientMap.put("연어", 93);
        ingredientMap.put("올리브유", 94);
        ingredientMap.put("차이브", 95);
        ingredientMap.put("페스토소스", 96);
        ingredientMap.put("가리비", 97);
        ingredientMap.put("가재새우", 98);
        ingredientMap.put("꼴뚜기", 99);
        ingredientMap.put("루콜라", 100);
        ingredientMap.put("문어", 101);
        ingredientMap.put("샐러리", 102);
        ingredientMap.put("소스", 103);
        ingredientMap.put("쭈꾸미", 104);
        ingredientMap.put("참소라살", 105);
        ingredientMap.put("트레비스", 106);
        ingredientMap.put("재첩", 107);
        ingredientMap.put("계란노른자", 108);
        ingredientMap.put("도미", 109);
        ingredientMap.put("바질", 110);
        ingredientMap.put("스위트마조람", 111);
        ingredientMap.put("오레가노", 112);
        ingredientMap.put("올리브", 113);
        ingredientMap.put("케이퍼", 114);
        ingredientMap.put("화이트와인", 115);
        ingredientMap.put("바나나", 116);
        ingredientMap.put("", 117);
        ingredientMap.put("", 118);
        ingredientMap.put("", 119);
        ingredientMap.put("", 120);
        ingredientMap.put("", 121);
        ingredientMap.put("", 122);
        ingredientMap.put("", 123);
        ingredientMap.put("", 124);
        ingredientMap.put("", 125);
        ingredientMap.put("", 126);
        ingredientMap.put("", 127);
        ingredientMap.put("", 128);
        ingredientMap.put("", 129);
        ingredientMap.put("", 130);
        ingredientMap.put("", 131);
        ingredientMap.put("", 132);
        ingredientMap.put("", 133);
        ingredientMap.put("", 134);
        ingredientMap.put("", 135);
        ingredientMap.put("", 136);
        ingredientMap.put("", 137);
        ingredientMap.put("", 138);
        ingredientMap.put("", 139);
        ingredientMap.put("", 140);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentOcrBinding = FragmentOcrBinding
                .inflate(inflater, container, false);
        return fragmentOcrBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        startCam();
        fragmentOcrBinding.clickBtn.setOnClickListener(onClickListener);
    }

    //카메라 시작하기
    private void startCam(){
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    imageCapture = new ImageCapture.Builder()
                            .setTargetRotation(getView().getDisplay().getRotation())
                            .build();
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    Preview preview = new Preview.Builder()
                            .build();
                    CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();
                    preview.setSurfaceProvider(fragmentOcrBinding.previewView.getSurfaceProvider());
                    Camera camera = cameraProvider.bindToLifecycle(getViewLifecycleOwner(),cameraSelector, imageCapture, preview);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, cameraExecutor);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            onclick();
        }
    };

    private void onclick() {
        imageCapture.takePicture(cameraExecutor,
            new ImageCapture.OnImageCapturedCallback() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onCaptureSuccess(ImageProxy image) {
                    // insert your code here.
                    try {
                        cameraProviderFuture.get().shutdown();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    @SuppressLint("UnsafeOptInUsageError")

                    Bitmap bitmap = ImageProxyToBitmap(image);
                    Bitmap rebitmap = scaleBitmapDown(bitmap, 640);

                    // Convert bitmap to base64 encoded string
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    rebitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();
                    String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                    // 3. cloud vision api 사용하기 위해 JSON reguest 생성
                    JsonObject request = new JsonObject();
                    // Add image to request
                    JsonObject jsonImage = new JsonObject();
                    jsonImage.add("content", new JsonPrimitive(base64encoded));
                    request.add("image", jsonImage);
                    //Add features to the request
                    JsonObject feature = new JsonObject();
                    feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
                    // Alternatively, for DOCUMENT_TEXT_DETECTION:
                    //feature.add("type", new JsonPrimitive("DOCUMENT_TEXT_DETECTION"));
                    JsonArray features = new JsonArray();
                    features.add(feature);
                    request.add("features", features);

                    //언어 감지
                    JsonObject imageContext = new JsonObject();
                    JsonArray languageHints = new JsonArray();
                    languageHints.add("ko"); //한국어
                    imageContext.add("languageHints", languageHints);
                    request.add("imageContext", imageContext);

                    // 4.함수 호출
                    annotateImage(request.toString())
                        .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
                            @Override
                            public void onComplete(@NonNull Task<JsonElement> task) {
                                if (!task.isSuccessful()) {
                                    // Task failed with an exception
                                    Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                                    Log.d("request", task.getException().toString());
                                    startCam();
                                } else {
                                    // Task completed successfully
                                    //인식된 텍스트를 text 필드에 저장
                                    if (task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("fullTextAnnotation") != null) {
                                        JsonObject annotation = task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("fullTextAnnotation").getAsJsonObject();
                                        ocrFragmentListener.ocrSuccess(checkIngredients(getOCRIngredientList(annotation)));
                                        Log.d("IngredientList", checkIngredients(getOCRIngredientList(annotation)).toString());
                                        Toast.makeText(requireContext(), checkIngredients(getOCRIngredientList(annotation)).toString(), Toast.LENGTH_LONG)
                                                .show();
                                    } else {
                                        ocrFragmentListener.ocrSuccess(null);
                                    }
                                }
                            }
                        });
                    super.onCaptureSuccess(image);
                }
                @Override
                public void onError(ImageCaptureException error) {
                    super.onError(error);

                }
            }
        );
    }

    //imageProxy to Bitmap
    private Bitmap ImageProxyToBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer byteBuffer = planeProxy.getBuffer();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    //Bitmap scale down
    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    // 2. 함수 호출을 위한 메서드
    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
            .getHttpsCallable("annotateImage")
            .call(requestJson)
            .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
                @Override
                public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
                    // This continuation runs on either success or failure, but if the task
                    // has failed then getResult() will throw an Exception which will be
                    // propagated down.
                    return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                }
            });
    }

    private ArrayList<String> getOCRIngredientList(JsonObject annotation){

        ArrayList<String> ingredientList = new ArrayList<> (Arrays.asList(""));

        //이미지 영역과 관련된 정보를 가져오기
        for (JsonElement page : annotation.get("pages").getAsJsonArray()) {
            StringBuilder pageText = new StringBuilder();
            for (JsonElement block : page.getAsJsonObject().get("blocks").getAsJsonArray()) {
                StringBuilder blockText = new StringBuilder();
                for (JsonElement para : block.getAsJsonObject().get("paragraphs").getAsJsonArray()) {
                    StringBuilder paraText = new StringBuilder();
                    for (JsonElement word : para.getAsJsonObject().get("words").getAsJsonArray()) {
                        StringBuilder wordText = new StringBuilder();
                        for (JsonElement symbol : word.getAsJsonObject().get("symbols").getAsJsonArray()) {
                            wordText.append(symbol.getAsJsonObject().get("text").getAsString());
                        }
                        paraText.append(wordText.toString()).append(" ");
                        ingredientList.add(wordText.toString());
                        int textSize = ingredientList.size();
                        String paymentText = ingredientList.get(textSize - 2);
                        String priceText = paymentText + ingredientList.get(textSize - 1);
                        if (priceText.equals("결제금액")) { return ingredientList; }
                    }
                    blockText.append(paraText);
                    Log.d("OCR", paraText.toString());
                }
                pageText.append(blockText);
            }
        }
        return ingredientList;
    }

    private ArrayList<String> checkIngredients(ArrayList<String> textList) {
        ArrayList<String> ingredientList = new ArrayList<>();
        for(String ingredient : textList) {
            checkIngredient(ingredientList,ingredient);
        }
        return ingredientList;
    }

    //영수증 text와 식재료값 비교
    private void checkIngredient(ArrayList<String> checkList, String ingredient){
        for(int j=0; j<ingredient.length(); j++) {
            for(int i=j+1; i<=ingredient.length(); i++){
                ingredient.substring(j,i);
                if(ingredientMap.containsKey(ingredient.substring(j,i))){
                    checkList.add(ingredient.substring(j,i));
                    checkIngredient(checkList, ingredient.substring(i));
                    return;
                }
            }
        }
    }

    private void readDataFromCsv(String filePath) throws IOException {
        CSVReader reader = new CSVReader(new FileReader(filePath)); // 1
        String [] nextLine;
        while ((nextLine = reader.readNext()) != null) {   // 2
            for (int i = 0; i < nextLine.length; i++) {
                System.out.println(i + " " + nextLine[i]);
            }
            System.out.println();
        }
    }


}