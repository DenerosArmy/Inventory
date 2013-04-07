package com.denerosarmy.inventory;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.lang.Thread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class ThumbnailAdapter extends ArrayAdapter<Item>{

    private final Context context;
    private final Item[] items;
    private final Integer[] itemCounts;
    private CompartmentAdapter compartment;

    @SuppressLint("NewApi")
    public ThumbnailAdapter(Context context, Hashtable<Item, Integer> itemsAndCounts) {
        super(context, R.layout.thumbnail, Collections.list(itemsAndCounts.keys()));
        //ArrayList<Item> lst = new ArrayList<Item>();
        //lst.addAll((Collection) Arrays.asList(itemsAndCounts.keySet().toArray()));
        //super(context, R.layout.thumbnail, Arrays.copyOf(itemsAndCounts.keySet().toArray(), itemsAndCounts.size(), Item[].class));
        this.context = context;
        this.compartment = compartment;
        this.items = Arrays.copyOf(itemsAndCounts.keySet().toArray(), itemsAndCounts.size(), Item[].class);
        java.util.Arrays.sort(this.items);
        this.itemCounts = new Integer[items.length];
        for (int i=0; i<items.length; i++){
            this.itemCounts[i] = itemsAndCounts.get(this.items[i]);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	Item item = items[position];
    	
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tile = inflater.inflate(R.layout.thumbnail, parent, false);
        tile.setBackground(item.getImg());
        TextView label = (TextView) tile.findViewById(R.id.label);
        TextView count = (TextView) tile.findViewById(R.id.count);
        label.setText(item.getName());
        if (itemCounts[position] > 1){
            count.setText(itemCounts[position].toString());
        }else{
            tile.findViewById(R.id.counter).setBackgroundColor(00000000);
            count.setText("");
        }

        // Animations
        if (item.toBeDeleted) {
        	Animation fadeout = AnimationHelper.createFadeoutAnimation();
        	AnimationHelper.animate(tile, fadeout);
            deleteOnAnimationComplete(fadeout, item);
            this.remove(item);
        }
        if (item.isNew) {
        	Animation fadein = AnimationHelper.createFadeInAnimation();
        	AnimationHelper.animate(tile, fadein);
        	item.isNew = false;
        }   
        return tile;
    }

    private void deleteOnAnimationComplete(Animation fadeout, final Item item) {
        fadeout.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                item.remove();
                notifyDataSetChanged();
            }
            
        });
    }
}
