package com.denerosarmy.inventory;

import android.widget.LinearLayout.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class CompartmentAdapter extends ArrayAdapter<Compartment>{

    protected Context context;
    protected Compartment[] compartments;
    protected boolean autoUpdate;

    public CompartmentAdapter(Context context){
        super(context, R.layout.compartment, Container.inst().getComps());
        this.context = context;
        this.compartments = Container.inst().getComps();
        this.autoUpdate = true;
    }

    public CompartmentAdapter(Context context, Compartment[] compartments){
        super(context, R.layout.compartment, compartments);
        this.context = context;
        this.compartments = compartments;
        this.autoUpdate = false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (autoUpdate){
            this.compartments = Container.inst().getComps();
        }
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.compartment, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        GridView gridView = (GridView) rowView.findViewById(R.id.itemGrid);
        textView.setText(compartments[position].getName());
        gridView.setAdapter(new ThumbnailAdapter(context, compartments[position].getItemsAndCounts()));
        gridView.setLayoutParams(new LayoutParams(-1, 2 * (110*(1+(int)((compartments[position].getSize()-1)/3)))));
        return rowView;
    }
} 
