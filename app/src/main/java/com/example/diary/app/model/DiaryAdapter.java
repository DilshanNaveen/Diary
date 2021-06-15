package com.example.diary.app.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.diary.app.R;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Diary> ItemList;
    private List<Diary> ItemListFiltered;

    public DiaryAdapter(Context context, ArrayList<Diary> itemList) {
        this.context = context;
        ItemList = itemList;
        ItemListFiltered = itemList;
    }

    @Override
    public int getCount() {
        return ItemListFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return ItemListFiltered.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.count = ItemList.size();
                    filterResults.values = ItemList;

                }else{
                    List<Diary> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for(Diary itemsModel: ItemList){
                        if(itemsModel.getSubject().contains(searchStr) || itemsModel.getDateTime().contains(searchStr)){
                            resultsModel.add(itemsModel);
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;
                        }
                    }


                }

                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                ItemListFiltered = (List<Diary>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }
}