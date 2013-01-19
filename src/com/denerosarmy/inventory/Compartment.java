package com.denerosarmy.inventory;

import android.widget.ArrayAdapter;
import java.util.*;

public class Compartment{

    private String id;
    private String name;
    private Hashtable<String, Item> itemMap;

    public Compartment(String id, String name){
        this.id = id;
        this.name = name;
        this.itemMap = new Hashtable<String, Item>();
        Container.inst().addComp(this);
    }

    protected void putItem(Item item){
        this.itemMap.put(item.getId(), item);
    }

    protected Item getItem(String itemId){
        return this.itemMap.get(itemId);
    }

    protected Item popItem(String itemId){
        Item item = this.getItem(itemId);
        this.itemMap.remove(itemId);
        return item;
    }

    protected Hashtable<String, Integer> getItems(){
        // Returns <K,V> = <ItemName, Count>
        Hashtable<String, Integer> items = new Hashtable<String, Integer>();
        for (Item item:itemMap.values()){
            if (items.containsKey(item.getName())){
                items.put(item.getName(), items.get(item.getName())+1);
            }else{
                items.put(item.getName(), 1);
            }
        }
        return items;
    }

    protected ArrayList<String> getItemList(){
        Hashtable<String, Integer> items = this.getItems();
        ArrayList<String> itemList = new ArrayList<String>();
        for (String item:items.keySet()){
            if (items.get(item) > 1){
                itemList.add(item+" ("+items.get(item)+")");
            }else{
                itemList.add(item);
            }
        }
        return itemList;
    }

    protected String getId(){
        return this.id;
    }

    protected String getName(){
        return this.name;
    }

}
