package com.example.recipify;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;


class ListViewLayout extends LinearLayout {

    private TextView btn;
    public ListViewLayout(Context context, LauncherActivity.ListItem item){
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listlayout,this,true);

        btn = (TextView)findViewById(R.id.btn);
        btn.setText(item.getText());
    }

    public void setText(String text){
        btn.setText(text);
    }
}