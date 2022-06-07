package com.example.recipify;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
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

import com.example.recipify.databinding.FragmentOcrBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.xml.transform.Result;

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
        ingredientMap.put("바나나", 63);

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
                                        ocrFragmentListener.ocrSuccess(checkIngredients(getTextList(annotation)));
                                        Log.d("IngredientList", checkIngredients(getTextList(annotation)).toString());
                                        Toast.makeText(requireContext(), checkIngredients(getTextList(annotation)).toString(), Toast.LENGTH_LONG)
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

    private ArrayList<String> getTextList(JsonObject annotation){

        ArrayList<String> textList = new ArrayList<> (Arrays.asList(""));

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
                        textList.add(wordText.toString());
                        int textSize = textList.size();
                        String paymentText = textList.get(textSize - 2);
                        String priceText = paymentText + textList.get(textSize - 1);
                        if (priceText.equals("결제금액")) { return textList; }
                    }
                    blockText.append(paraText);
                    Log.d("OCR", paraText.toString());
                }
                pageText.append(blockText);
            }
        }
        return textList;
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

}