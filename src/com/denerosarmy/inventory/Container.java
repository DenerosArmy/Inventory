package com.denerosarmy.inventory;

import android.annotation.SuppressLint;
import android.content.Context;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.*;

public class Container implements Serializable{

    private static Container inst = null;
    private Hashtable<String, Compartment> compartmentMap;
    private LinkedList<Item> items;
    private String SAVEFILEDIR = "inventory_save";

    protected Container(){
        compartmentMap = new Hashtable<String, Compartment>();
        items = new LinkedList<Item>();
    }

    protected static Container inst() {
        if (inst == null) {
            inst = new Container();
        }
        return inst;
    }

    protected boolean hasItem(Item item){
        return items.contains(item);
    }

    protected Item getItemNamed(String itemName){
        for (Item item:items){
            if (Matcher.matches(item.getName(), itemName)){
                return item;
            }
        }
        return null;
    }

    protected LinkedList<Item> getMissingItems(){
        LinkedList<Item> missingItems = new LinkedList<Item>();
        for (Item item:items){
            if (!item.inContainer()){
                missingItems.add(item);
            }
        }
        return missingItems;
    }

    protected Compartment getComp(String compId){
        return this.compartmentMap.get(compId);
    }
    
    protected void addComp(Compartment compartment){
        this.compartmentMap.put(compartment.getId(), compartment);
    }

    protected void addItem(Item item){
        this.items.add(item);
    }

    @SuppressLint("NewApi")
    protected Compartment[] getComps(){
        return Arrays.copyOf(this.compartmentMap.values().toArray(), this.compartmentMap.size(), Compartment[].class);
    }

    //protected boolean save(){
        //FileOutputStream fos = Context.openFileOutput(SAVEFILEDIR, Context.MODE_PRIVATE);
        //fos.write(this.getBytes());
        //fos.close();
    //}

    //protected static Container load(){
        //FileInputStream fis = context.openFileInput(SAVEFILEDIR);
        //ObjectInputStream is = new ObjectInputStream(fis);
        //Container container = (Container) is.readObject();
        //is.close();
        //return container;
    //}

}
