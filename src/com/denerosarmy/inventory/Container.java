package com.denerosarmy.inventory;

import android.annotation.SuppressLint;
import android.content.Context;
import java.io.*;
import java.util.*;
import java.lang.ClassNotFoundException;

public class Container implements Serializable{

    private static final String SAVEFILEDIR = "inventory_save";
    private static Container inst = null;
    private Hashtable<String, Compartment> compartmentMap;
    private LinkedList<Item> items;

    protected Container(){
        compartmentMap = new Hashtable<String, Compartment>();
        items = new LinkedList<Item>();
    }

    protected static Container inst() {
        if (inst == null){
            if ((inst = loadContainer()) == null){
                inst = new Container();
            }
        }
        return inst;
    }

    protected static Container loadContainer(){
        try{
            FileInputStream fis = Inventory.getContext().openFileInput(SAVEFILEDIR);
            ObjectInputStream is = new ObjectInputStream(fis);
            Container container = (Container) is.readObject();
            is.close();
            return container;
        }catch (FileNotFoundException fnfe){
            System.err.println(fnfe);
        }catch (StreamCorruptedException sce){
            System.err.println(sce);
        }catch (OptionalDataException ode){
            System.err.println(ode);
        }catch (IOException ioe){
            System.err.println(ioe);
        }catch (ClassNotFoundException cnfe){
            System.err.println(cnfe);
        }
        return null;
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

    protected boolean save(){
        try{
            FileOutputStream fos = Inventory.getContext().openFileOutput(SAVEFILEDIR, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            return true;
        }catch (FileNotFoundException fnfe){
            System.err.println(fnfe);
        }catch (IOException ioe){
            System.err.println(ioe);
        }
        return false;
    }

}
