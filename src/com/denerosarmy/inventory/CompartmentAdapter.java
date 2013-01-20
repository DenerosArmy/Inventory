package com.denerosarmy.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class CompartmentAdapter extends ArrayAdapter<Compartment>{

    private Context context;
    private Compartment[] compartments;

    public CompartmentAdapter(Context context) {
        super(context, R.layout.compartment, Container.inst().getComps());
        this.context = context;
        this.compartments = Container.inst().getComps();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.compartments = Container.inst().getComps();
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.compartment, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        GridView gridView = (GridView) rowView.findViewById(R.id.itemGrid);
        textView.setText(compartments[position].getName());
        gridView.setAdapter(new ThumbnailAdapter(context, compartments[position].getItemsAndCounts()));

        final float scale = getContext().getResources().getDisplayMetrics().density;
        System.out.println(compartments[position].getNumTiles());
        double rows = Math.ceil(compartments[position].getNumTiles()/3.0);
        int width = -1;
        int height = (int) (110 * rows * scale + 0.5f);
        gridView.setLayoutParams(new LayoutParams(width, height));
        return rowView;
    }
} 
