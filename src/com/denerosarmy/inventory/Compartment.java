package com.denerosarmy.inventory;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.http.NameValuePair;
import java.util.Hashtable;
import org.apache.http.NameValuePair;
import java.util.* ;
import org.apache.http.message.BasicNameValuePair;

public class Compartment implements Serializable{

    private String id;
    public String name;
    private Hashtable<String, Item> itemMap;
    public List<NameValuePair> itemMapList;
    public Compartment(String id, String name){
        this.id = id;
        this.name = name;
        this.itemMap = new Hashtable<String, Item>();
        this.itemMapList = new ArrayList<NameValuePair>();
        Container.inst().addComp(this);
        // save
    }
    
    protected int getSize() {
    	return this.itemMap.size();
    }
    
    protected int getNumTiles() {
    	return getItemNames().size();
    }

    protected void putItem(Item item){
        this.itemMap.put(item.getId(), item);
        this.itemMapList.add(new BasicNameValuePair(item.getId(),item.getName()));
    }

    protected Item getItem(String itemId){
        return this.itemMap.get(itemId);
    }

    protected Item popItem(String itemId){
        Item item = this.getItem(itemId);
        this.itemMap.remove(itemId);
        this.itemMapList.remove(new BasicNameValuePair(item.getId(),item.getName()));
        return item;
    }

    protected Hashtable<Item, Integer> getItemsAndCounts(String query){
        // Does a search for [query] and returns <K,V> = <Item, Count>
        Hashtable<String, Item> itemNameMap = new Hashtable<String, Item>();
        Hashtable<Item, Integer> itemsAndCounts = new Hashtable<Item, Integer>();
        for (Item item:itemMap.values()){
            if (Matcher.matches(item.getName(), query)){
                if (itemNameMap.containsKey(item.getName())){
                    if (itemsAndCounts.containsKey(item)){
                        itemsAndCounts.put(itemNameMap.get(item.getName()), itemsAndCounts.get(itemNameMap.get(item.getName()))+1);
                    }else{
                        itemsAndCounts.put(itemNameMap.get(item.getName()), 1);
                    }
                }else{
                    itemNameMap.put(item.getName(), item);
                    itemsAndCounts.put(item, 1);
                }
            }
        }
        return itemsAndCounts;
    }

    protected Hashtable<Item, Integer> getItemsAndCounts(){
        // Returns <K,V> = <Item, Count>
        Hashtable<String, Item> itemNameMap = new Hashtable<String, Item>();
        Hashtable<Item, Integer> itemsAndCounts = new Hashtable<Item, Integer>();
        for (Item item:itemMap.values()){
            if (itemNameMap.containsKey(item.getName())){
                if (itemsAndCounts.containsKey(itemNameMap.get(item.getName()))){
                    itemsAndCounts.put(itemNameMap.get(item.getName()), itemsAndCounts.get(itemNameMap.get(item.getName())) + Integer.valueOf(1));
                }else{
                    itemsAndCounts.put(itemNameMap.get(item.getName()), Integer.valueOf(1));
                }
            }else{
                itemNameMap.put(item.getName(), item);
                itemsAndCounts.put(item, Integer.valueOf(1));
            }
        }
        return itemsAndCounts;
    }

    protected Hashtable<String, Integer> getItemNames(){
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

    @SuppressLint("NewApi")
    protected Item[] getItemList(){
        Object[] objs = this.itemMap.values().toArray();
        Item[] items = Arrays.copyOf(objs, objs.length, Item[].class);
        return items;
    }

    protected ArrayList<String> getStringItemList(){
        Hashtable<String, Integer> items = this.getItemNames();
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
