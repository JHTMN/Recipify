package com.example.recipify;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;


public class listlayout extends BaseAdapter {
    private Context mContext;
    private List<ListItem> listItem = new ArrayList<ListItem>();

    public void ListViewAdapter(Context context){
        mContext = context;
    }

    public void addItem(ListItem item){
        listItem.add(item);
    }

    public int getCount() {
        return listItem.size();
    }

    public Object getItem(int i) {
        return listItem.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ListViewLayout itemView;
        if(view ==null){
            itemView = new ListViewLayout(mContext,listItem.get(i));
        }else{
            itemView = (ListViewLayout) view;

            itemView.setText(listItem.get(i).getText());
        }

        return itemView;
    }
}
