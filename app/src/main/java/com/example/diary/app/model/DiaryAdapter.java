package com.example.diary.app.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.diary.app.R;

import java.util.ArrayList;

public class DiaryAdapter extends ArrayAdapter<Diary> {
    Context context;
    ArrayList<Diary> ItemList;

    public DiaryAdapter(@NonNull Context context, ArrayList<Diary> ItemList) {
        super(context, 0,ItemList);
        this.context = context;
        this.ItemList = ItemList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){//if view null then create new view
            view= LayoutInflater.from(context).inflate(R.layout.listview_shape,parent,false);//for creating view
        }

        Diary item = ItemList.get(position);

        //finding listview shape component
        TextView subject = view.findViewById(R.id.subjectListViewShapeId);
        TextView date = view.findViewById(R.id.dateListViewShapeId);
        //return super.getView(position, convertView, parent);


        //setting listview shape component to arrryList
        subject.setText(item.getSubject());
        date.setText(item.getDateTime());

        return view;
    }
}
