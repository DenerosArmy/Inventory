package com.denerosarmy.inventory;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ThumbnailAdapter extends ArrayAdapter<Item>{

    private final Context context;
    private final Item[] items;

    public ThumbnailAdapter(Context context, Item[] items){
        super(context, R.layout.thumbnail, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	/*
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.thumbnail, parent, false);
        rowView.setBackgroundResource(R.id.icon);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(items[position].getName());
        //imageView.setImageResource(items[position].getPic());
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
        return rowView;*/
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.thumbnail, parent, false);
        rowView.setBackgroundResource(items[position].getPic());
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(items[position].getName());
        return rowView;
    }
} 
