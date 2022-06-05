package com.syeon.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<HashMap<String, Object>> ingredientMaps;

    private CalenderListener calenderListener;

    public void setCalenderListener(CalenderListener calenderListener) {
        this.calenderListener = calenderListener;
    }


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_view,parent,false);
        IngredientViewHolder ingredientViewHolder = new IngredientViewHolder(view);
        return ingredientViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewholder, @SuppressLint("RecyclerView") int position) {

        HashMap<String, Object> ingredientHashMap = (HashMap<String,Object>)ingredientMaps.get(position);
        String ingredient = (String) ingredientHashMap.get("ingredient");
        ingredientViewholder.ingredientText.setText(ingredient);

        /*ingredientViewholder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calenderListener.calender(position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return ingredientMaps.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientText;
        TextView date;
        LinearLayout layoutTodo;
        CheckBox checkBox;
        Button deleteButton;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientText = itemView.findViewById(R.id.ingredient_text_view);
            //date = itemView.findViewById(R.id.date_text_view);
            layoutTodo = itemView.findViewById(R.id.layoutTodo);
            checkBox = itemView.findViewById(R.id.checkBox);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            //버튼 클릭 시 SQLite에서 데이터 삭제
            deleteButton.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    //CheckBox의 String 가져오기
                    String TODO = (String) checkBox.getText();
                    deleteToDo(TODO);
                    Toast.makeText(v.getContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }


                Context context;

                private void deleteToDo(String TODO){
                    //테이블을 삭제하는 sql문 delete...
                    String deleteSql = "delete from " + NoteDatabase.TABLE_NOTE + " where " + "  TODO = '" + TODO+"'";
                    NoteDatabase database = NoteDatabase.getInstance(context);
                    //삭제하는 sql문 실행
                    database.execSQL(deleteSql);
                }
            });


        }
        //EditText에서 입력받은 checkBox의 텍스트를 checkBox의 Text에 넣을 수 있게 하는 메서드
        public void setItem(Note item){
            checkBox.setText(item.getTodo());
        }

        //아이템들을 담은 LinearLayout을 보여주게하는 메서드
        public void setLayout(){
            layoutTodo.setVisibility(View.VISIBLE);
        }
    }

    public void setIngredientMaps(ArrayList<HashMap<String, Object>> ingredientMaps){
        this.ingredientMaps = ingredientMaps;
    }

}
