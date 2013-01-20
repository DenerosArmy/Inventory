package com.denerosarmy.inventory;

import java.util.Arrays;
import java.util.Hashtable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThumbnailAdapter extends ArrayAdapter<Item>{

    private final Context context;
    private final Item[] items;
    private final Integer[] itemCounts;
    private ViewGroup mCompartment;

    @SuppressLint("NewApi")
	public ThumbnailAdapter(Context context, Hashtable<Item, Integer> itemsAndCounts){
        super(context, R.layout.thumbnail, Arrays.copyOf(itemsAndCounts.keySet().toArray(), itemsAndCounts.size(), Item[].class));
        this.context = context;
        this.items = Arrays.copyOf(itemsAndCounts.keySet().toArray(), itemsAndCounts.size(), Item[].class);
        java.util.Arrays.sort(this.items);
        this.itemCounts = new Integer[items.length];
        for (int i=0; i<items.length; i++){
            this.itemCounts[i] = itemsAndCounts.get(this.items[i]);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	mCompartment = parent;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tile = inflater.inflate(R.layout.thumbnail, parent, false);
        tile.setBackgroundResource(items[position].getPic());
        TextView label = (TextView) tile.findViewById(R.id.label);
        TextView count = (TextView) tile.findViewById(R.id.count);
        label.setText(items[position].getName());
        if (itemCounts[position] > 1){
            count.setText(itemCounts[position].toString());
        }else{
            tile.findViewById(R.id.counter).setBackgroundColor(00000000);
            count.setText("");
        }
        if (items[position].isNew) {
        	Animation fadein = AnimationHelper.createFadeInAnimation();
        	AnimationHelper.animate(tile, fadein);
        	items[position].isNew = false;
        }   
        return tile;
    }
} 
