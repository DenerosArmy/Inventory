package com.denerosarmy.inventory;

import java.util.*;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class CompartmentAdapter extends ArrayAdapter<Compartment>{

    private final Context context;
    private final Compartment[] compartments;

    public CompartmentAdapter(Context context, Compartment[] compartments) {
        super(context, R.layout.compartment, compartments);
        this.context = context;
        this.compartments = compartments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.compartment, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        GridView gridView = (GridView) rowView.findViewById(R.id.itemGrid);
        textView.setText(compartments[position].getName());
        gridView.setAdapter(new ThumbnailAdapter(context, compartments[position].getItemsAndCounts()));
        // Change the icon for Windows and iPhone
        return rowView;
    }
} 
