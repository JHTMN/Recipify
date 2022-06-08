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

import androidx.activity.OnBackPressedCallback;
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

    private HashMap<String, Object> ingredientMap;

    interface OcrFragmentListener{
        void ocrSuccess(ArrayList<String> textList); //인식된 식재료를 넘기기 위해
        void ocrVisibleView();
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

        ingredientMap = new HashMap<String, Object>();

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ocrFragmentListener.ocrVisibleView();
                setEnabled(false);
                getActivity().onBackPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

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
        ingredientMap.put("꼬치", 116);
        ingredientMap.put("꿀", 117);
        ingredientMap.put("느타리버섯", 118);
        ingredientMap.put("배즙", 119);
        ingredientMap.put("생강즙", 120);
        ingredientMap.put("토마토케첩", 121);
        ingredientMap.put("콩가루", 122);
        ingredientMap.put("생강", 123);
        ingredientMap.put("배추", 124);
        ingredientMap.put("연겨자", 125);
        ingredientMap.put("파프리카", 126);
        ingredientMap.put("실고추", 127);
        ingredientMap.put("우엉", 128);
        ingredientMap.put("다진양파", 129);
        ingredientMap.put("토마토", 130);
        ingredientMap.put("갈치", 131);
        ingredientMap.put("닭고기", 132);
        ingredientMap.put("들깨", 133);
        ingredientMap.put("초고추장", 134);
        ingredientMap.put("칼국수", 135);
        ingredientMap.put("무명실", 136);
        ingredientMap.put("양배추", 137);
        ingredientMap.put("파슬리가루", 138);
        ingredientMap.put("목이버섯", 139);
        ingredientMap.put("생선살", 140);
        ingredientMap.put("파인애플", 141);
        ingredientMap.put("양파즙", 142);
        ingredientMap.put("월계수잎", 143);
        ingredientMap.put("통깨", 144);
        ingredientMap.put("레드와인", 145);
        ingredientMap.put("버터", 146);
        ingredientMap.put("빵가루", 147);
        ingredientMap.put("은행", 148);
        ingredientMap.put("갓", 149);
        ingredientMap.put("굴", 150);
        ingredientMap.put("된장", 151);
        ingredientMap.put("새우젓", 152);
        ingredientMap.put("젓국", 153);
        ingredientMap.put("김칫국물", 154);
        ingredientMap.put("청국장", 155);
        ingredientMap.put("바지락", 156);
        ingredientMap.put("순두부", 157);
        ingredientMap.put("동태", 158);
        ingredientMap.put("가래떡", 159);
        ingredientMap.put("유부", 160);
        ingredientMap.put("단무지", 161);
        ingredientMap.put("게맛살", 162);
        ingredientMap.put("해파리", 163);
        ingredientMap.put("멸치젓", 164);
        ingredientMap.put("총각무", 165);
        ingredientMap.put("배주머니", 166);
        ingredientMap.put("꽃게", 167);
        ingredientMap.put("다시다", 168);
        ingredientMap.put("싸리버섯", 169);
        ingredientMap.put("아귀", 170);
        ingredientMap.put("조기", 171);
        ingredientMap.put("명란", 172);
        ingredientMap.put("쑥갓", 173);
        ingredientMap.put("쌀뜨물", 174);
        ingredientMap.put("우거지", 175);
        ingredientMap.put("곤약", 176);
        ingredientMap.put("어묵", 177);
        ingredientMap.put("팽이버섯", 178);
        ingredientMap.put("낙지", 179);
        ingredientMap.put("선지", 180);
        ingredientMap.put("김", 181);
        ingredientMap.put("장조림", 182);
        ingredientMap.put("양", 183);
        ingredientMap.put("도가니", 184);
        ingredientMap.put("밀국수", 185);
        ingredientMap.put("사골", 186);
        ingredientMap.put("쇠뼈", 187);
        ingredientMap.put("까나리액젓", 188);
        ingredientMap.put("석이버섯", 189);
        ingredientMap.put("연근", 190);
        ingredientMap.put("조갯살", 191);
        ingredientMap.put("냉이", 192);
        ingredientMap.put("더덕", 193);
        ingredientMap.put("꽈리고추", 194);
        ingredientMap.put("마른오징어", 195);
        ingredientMap.put("메주콩", 196);
        ingredientMap.put("칵테일새우", 197);
        ingredientMap.put("고추냉이", 198);
        ingredientMap.put("참치", 199);
        ingredientMap.put("꽁치", 200);
        ingredientMap.put("넛맥", 201);
        ingredientMap.put("라면", 202);
        ingredientMap.put("햄", 203);
        ingredientMap.put("와사비", 204);
        ingredientMap.put("치커리", 205);
        ingredientMap.put("쪽파", 206);
        ingredientMap.put("고등어", 207);
        ingredientMap.put("마른고추", 208);
        ingredientMap.put("가지", 209);
        ingredientMap.put("건새우", 210);
        ingredientMap.put("골뱅이", 211);
        ingredientMap.put("소면", 212);
        ingredientMap.put("잣가루", 213);
        ingredientMap.put("베이컨", 214);
        ingredientMap.put("생크림", 215);
        ingredientMap.put("스파게티", 216);
        ingredientMap.put("수삼", 217);
        ingredientMap.put("은박컵", 218);
        ingredientMap.put("후르츠칵테일", 219);
        ingredientMap.put("슬라이스치즈", 220);
        ingredientMap.put("식빵", 221);
        ingredientMap.put("양상추", 222);
        ingredientMap.put("피클", 223);
        ingredientMap.put("마요네즈", 224);
        ingredientMap.put("머스터드", 225);
        ingredientMap.put("컬리플라워", 226);
        ingredientMap.put("딸기", 227);
        ingredientMap.put("사과", 228);
        ingredientMap.put("자몽", 229);
        ingredientMap.put("앤다이브", 230);
        ingredientMap.put("훈제연어", 231);
        ingredientMap.put("레몬드레싱", 232);
        ingredientMap.put("레몬주스", 233);
        ingredientMap.put("시금치", 234);
        ingredientMap.put("파마산치즈", 235);
        ingredientMap.put("홀토마토", 236);
        ingredientMap.put("프랑크소시지", 237);
        ingredientMap.put("곱창", 238);
        ingredientMap.put("우동면", 239);
        ingredientMap.put("미더덕", 240);
        ingredientMap.put("강력분", 241);
        ingredientMap.put("건포도", 242);
        ingredientMap.put("검은깨", 243);
        ingredientMap.put("드라이이스트", 244);
        ingredientMap.put("오렌지마멀레이드", 245);
        ingredientMap.put("무순", 246);
        ingredientMap.put("깨", 247);
        ingredientMap.put("녹두", 248);
        ingredientMap.put("솔잎", 249);
        ingredientMap.put("쑥", 250);
        ingredientMap.put("토란", 251);
        ingredientMap.put("튀김가루", 252);
        ingredientMap.put("누룽지", 253);
        ingredientMap.put("브로콜리", 254);
        ingredientMap.put("찬밥", 255);
        ingredientMap.put("쌀가루", 256);
        ingredientMap.put("슈가파우더", 257);
        ingredientMap.put("아몬드", 258);
        ingredientMap.put("고구마줄기", 259);
        ingredientMap.put("들기름", 250);
        ingredientMap.put("곶감", 261);
        ingredientMap.put("호두", 262);
        ingredientMap.put("감자전분", 263);
        ingredientMap.put("굴소스", 264);
        ingredientMap.put("떡국떡", 265);
        ingredientMap.put("다진고추", 266);
        ingredientMap.put("취나물", 267);
        ingredientMap.put("건새우가루", 268);
        ingredientMap.put("겨자잎", 269);
        ingredientMap.put("청경채", 270);
        ingredientMap.put("케일", 271);
        ingredientMap.put("국멸치", 272);
        ingredientMap.put("달래", 273);
        ingredientMap.put("청양고추", 274);
        ingredientMap.put("명란젓", 275);
        ingredientMap.put("봄동", 276);
        ingredientMap.put("옥수수", 277);
        ingredientMap.put("정향", 278);
        ingredientMap.put("송이버섯", 279);
        ingredientMap.put("말린새우", 280);
        ingredientMap.put("근대", 281);
        ingredientMap.put("레몬", 282);
        ingredientMap.put("맛국물", 283);
        ingredientMap.put("왜된장", 284);
        ingredientMap.put("생새우", 285);
        ingredientMap.put("콘프레이크", 286);
        ingredientMap.put("새우살", 287);
        ingredientMap.put("게", 288);
        ingredientMap.put("대하", 289);
        ingredientMap.put("고구마", 290);
        ingredientMap.put("녹차", 291);
        ingredientMap.put("모짜렐라치즈", 292);
        ingredientMap.put("포도", 293);
        ingredientMap.put("사우어크라우트", 294);
        ingredientMap.put("오곡", 295);
        ingredientMap.put("코다리", 296);
        ingredientMap.put("광어", 297);
        ingredientMap.put("연어알", 298);
        ingredientMap.put("전복", 299);
        ingredientMap.put("청어알", 300);
        ingredientMap.put("초밥", 301);
        ingredientMap.put("피조개", 302);
        ingredientMap.put("치즈", 303);
        ingredientMap.put("홍차티백", 304);
        ingredientMap.put("멸치다시물", 305);
        ingredientMap.put("무즙", 306);
        ingredientMap.put("비엔나소시지", 307);
        ingredientMap.put("액체육젓", 308);
        ingredientMap.put("인절미", 309);
        ingredientMap.put("바게트", 310);
        ingredientMap.put("피쉬소스", 311);
        ingredientMap.put("사이다", 312);
        ingredientMap.put("겨자가루", 313);
        ingredientMap.put("땅콩", 314);
        ingredientMap.put("랩", 315);
        ingredientMap.put("맛소금", 316);
        ingredientMap.put("매실장아찌", 317);
        ingredientMap.put("당근즙", 318);
        ingredientMap.put("시금치즙", 319);
        ingredientMap.put("고들빼기", 320);
        ingredientMap.put("멸치액젓", 321);
        ingredientMap.put("두릅", 322);
        ingredientMap.put("참나물", 323);
        ingredientMap.put("스위트콘", 324);
        ingredientMap.put("완두콩통조림", 325);
        ingredientMap.put("우스터소스", 326);
        ingredientMap.put("아스파라거스", 327);
        ingredientMap.put("땅콩버터", 328);
        ingredientMap.put("오이피클", 329);
        ingredientMap.put("체리", 330);
        ingredientMap.put("케첩", 331);
        ingredientMap.put("핫소스", 332);
        ingredientMap.put("춘권피", 333);
        ingredientMap.put("조미료", 334);
        ingredientMap.put("크림수프", 335);
        ingredientMap.put("순대", 336);
        ingredientMap.put("쫄면", 337);
        ingredientMap.put("떡", 338);
        ingredientMap.put("열무김치", 339);
        ingredientMap.put("이스트", 340);
        ingredientMap.put("쌀국수", 341);
        ingredientMap.put("아욱", 342);
        ingredientMap.put("해삼", 343);
        ingredientMap.put("두반장", 344);
        ingredientMap.put("새송이버섯", 345);
        ingredientMap.put("숙주나물", 346);
        ingredientMap.put("파스타", 347);
        ingredientMap.put("닭육수", 348);
        ingredientMap.put("오징어채", 349);
        ingredientMap.put("청장", 350);
        ingredientMap.put("새우젓국", 351);
        ingredientMap.put("대구", 352);
        ingredientMap.put("다진쇠고기", 353);
        ingredientMap.put("보리", 354);
        ingredientMap.put("들깻잎", 355);
        ingredientMap.put("토마토소스", 356);
        ingredientMap.put("파래", 357);
        ingredientMap.put("단호박", 358);
        ingredientMap.put("라이스페이퍼", 359);
        ingredientMap.put("키위", 360);
        ingredientMap.put("치즈가루", 361);
        ingredientMap.put("가쓰오브시", 362);
        ingredientMap.put("미소된장", 363);
        ingredientMap.put("양겨자", 364);
        ingredientMap.put("통후추", 365);
        ingredientMap.put("페투치네", 366);
        ingredientMap.put("오보로", 367);
        ingredientMap.put("게살", 368);
        ingredientMap.put("날치알", 369);
        ingredientMap.put("박고지", 370);
        ingredientMap.put("아보카도", 371);
        ingredientMap.put("초생강", 372);
        ingredientMap.put("조", 373);
        ingredientMap.put("냉동만두", 374);
        ingredientMap.put("메밀국수", 375);
        ingredientMap.put("맛조개", 376);
        ingredientMap.put("해초", 377);
        ingredientMap.put("산초가루", 378);
        ingredientMap.put("장어", 379);
        ingredientMap.put("장어대리", 380);
        ingredientMap.put("장어뼈", 381);
        ingredientMap.put("돈까스소스", 382);
        ingredientMap.put("바나나", 383);
        ingredientMap.put("타바스코", 384);
        ingredientMap.put("고추기름", 385);
        ingredientMap.put("간수", 386);
        ingredientMap.put("적채", 387);
        ingredientMap.put("사워크림", 388);
        ingredientMap.put("치자", 389);
        ingredientMap.put("깨소스", 390);
        ingredientMap.put("폰즈소스", 391);
        ingredientMap.put("고춧기름", 392);
        ingredientMap.put("생대구", 393);
        ingredientMap.put("채소", 394);
        ingredientMap.put("생밤", 395);
        ingredientMap.put("쌈다시마", 396);
        ingredientMap.put("무말랭이", 397);
        ingredientMap.put("육회", 398);
        ingredientMap.put("크림치즈", 399);
        ingredientMap.put("고수", 400);
        ingredientMap.put("쇠꼬리", 401);
        ingredientMap.put("팔각", 402);
        ingredientMap.put("숯", 403);
        ingredientMap.put("메주", 404);
        ingredientMap.put("엿기름", 405);
        ingredientMap.put("시럽", 406);
        ingredientMap.put("호박씨", 407);
        ingredientMap.put("순창콩된장", 408);
        ingredientMap.put("붉은갓", 409);
        ingredientMap.put("삭힌고추", 410);
        ingredientMap.put("녹차국수", 411);
        ingredientMap.put("우무", 412);
        ingredientMap.put("서리태콩", 413);
        ingredientMap.put("참깨", 414);
        ingredientMap.put("닭발", 415);
        ingredientMap.put("전어", 416);
        ingredientMap.put("전어젓갈", 417);
        ingredientMap.put("샐러드오일", 418);
        ingredientMap.put("타임", 419);
        ingredientMap.put("소꼬리", 420);
        ingredientMap.put("고기산적", 421);
        ingredientMap.put("멥쌀가루", 422);
        ingredientMap.put("액젓", 423);
        ingredientMap.put("바베큐소스", 424);
        ingredientMap.put("비트", 425);
        ingredientMap.put("절임무", 426);
        ingredientMap.put("오렌지", 427);
        ingredientMap.put("허니머스타드", 428);
        ingredientMap.put("참치캔", 429);
        ingredientMap.put("꽃빵", 430);
        ingredientMap.put("어장", 431);
        ingredientMap.put("장국", 432);
        ingredientMap.put("새싹채소", 433);
        ingredientMap.put("과일통조림", 434);
        ingredientMap.put("발효초", 435);
        ingredientMap.put("초콜릿", 436);
        ingredientMap.put("플레인요구르트", 437);
        ingredientMap.put("샐러드채소", 438);
        ingredientMap.put("메기", 439);
        ingredientMap.put("잉어", 440);
        ingredientMap.put("송어", 441);
        ingredientMap.put("그라나 빠다노", 442);
        ingredientMap.put("로즈마리", 443);
        ingredientMap.put("사라다나", 444);
        ingredientMap.put("이태리고추", 445);
        ingredientMap.put("칠리", 446);
        ingredientMap.put("볶은 흑임자", 447);
        ingredientMap.put("인삼", 448);
        ingredientMap.put("사과즙", 449);
        ingredientMap.put("시래기", 450);
        ingredientMap.put("감동젓", 451);
        ingredientMap.put("버섯", 452);
        ingredientMap.put("돛나물", 453);
        ingredientMap.put("매실액", 454);
        ingredientMap.put("파뿌리", 455);
        ingredientMap.put("다진쪽파", 456);
        ingredientMap.put("노각", 457);
        ingredientMap.put("고구마잎", 458);
        ingredientMap.put("피자치즈", 459);
        ingredientMap.put("호박잎", 460);
        ingredientMap.put("둥근호박", 461);
        ingredientMap.put("모닝빵", 462);
        ingredientMap.put("슬라이스햄", 463);
        ingredientMap.put("어린잎채소", 464);
        ingredientMap.put("라임", 465);
        ingredientMap.put("얼음", 466);
        ingredientMap.put("탄산수", 467);
        ingredientMap.put("허브", 468);
        ingredientMap.put("견과류", 469);
        ingredientMap.put("백오이", 470);
        ingredientMap.put("삶은계란", 471);
        ingredientMap.put("허브솔트", 472);
        ingredientMap.put("무채", 473);
        ingredientMap.put("당근잎", 474);
        ingredientMap.put("부침가루", 475);
        ingredientMap.put("건블루베리", 476);
        ingredientMap.put("콩비지", 477);

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

        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        // 1.Cloud Functions 인스턴스 초기화
        mFunctions = FirebaseFunctions.getInstance();

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
                                    if (task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("fullTextAnnotation") != null) {
                                        JsonObject annotation = task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("fullTextAnnotation").getAsJsonObject();
                                        ocrFragmentListener.ocrSuccess(checkIngredients(getOCRIngredientList(annotation)));
                                        //Log.d("IngredientList", checkIngredients(getOCRIngredientList(annotation)).toString());
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
    private void checkIngredient(ArrayList<String> ingredientList, String ingredient){
        for(int j = 0; j < ingredient.length(); j++) {
            for(int i = j + 1; i <= ingredient.length(); i++){
                ingredient.substring(j,i);
                if(ingredientMap.containsKey(ingredient.substring(j,i))){
                    ingredientList.add(ingredient.substring(j,i));
                    checkIngredient(ingredientList, ingredient.substring(i));
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