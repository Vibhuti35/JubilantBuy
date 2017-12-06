package com.example.tonycurrie.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapterCompare extends ArrayAdapter<CompareItems> {

    Context context;
    CompareItems rowItem;

    public CustomListViewAdapterCompare(Context context, int resourceId,
                                        List<CompareItems> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView store;
        TextView price;
        TextView url;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listcompare, null);
            holder = new ViewHolder();
            holder.store = (TextView)convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.url = (TextView) convertView.findViewById(R.id.url);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.store.setText(rowItem.getName());
        holder.price.setText(rowItem.getPrice());
        holder.url.setText(rowItem.getUrl());
        holder.url.setClickable(true);

        return convertView;
    }
}