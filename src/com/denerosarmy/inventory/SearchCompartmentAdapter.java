package com.denerosarmy.inventory;

import android.widget.LinearLayout.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class SearchCompartmentAdapter extends CompartmentAdapter{

    private String query;

    public SearchCompartmentAdapter(Context context, String query){
        super(context);
        this.query = query;
    }

    public SearchCompartmentAdapter(Context context, Compartment[] compartments, String query){
        super(context, compartments);
        this.query = query;
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
        gridView.setAdapter(new ThumbnailAdapter(context, compartments[position].getItemsAndCounts(query)));
        gridView.setLayoutParams(new LayoutParams(-1, 2 * (110*(1+(int)((compartments[position].getSize()-1)/3)))));
        return rowView;
    }

} 
