package com.denerosarmy.inventory;

import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import java.io.Serializable;
import java.util.Date;

public class Item implements Comparable, Serializable{

    private String id;
    private String name;
    private String compId;
    private Integer pic;
    private Drawable img;
    public boolean isNew;
    public boolean toBeDeleted;
    private Date created;

    public Item(String id, String name, Integer pic) {
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.img = null;
        this.isNew = true;
        this.toBeDeleted = false;
        this.created = new Date();
        Container.inst().addItem(this);
    }

    public Item(String id, String name, Drawable img) {
        this.id = id;
        this.name = name;
        this.pic = 0;
        this.img = img;
        this.isNew = true;
        this.toBeDeleted = false;
        this.created = new Date();
        Container.inst().addItem(this);
    }

    protected void putInto(String compId){
        this.compId = compId;
        Container.inst().getComp(compId).putItem(this);
        // save
    }

    protected void remove(){
        Container.inst().getComp(compId).popItem(this.getId());
        this.compId = null;
    }
    
    protected void scheduleDeletion() {
    	this.toBeDeleted = true;
    }
    
    protected String flip() { 
        System.out.println("FLIP CALLED FLIP CALLED");
        if (inContainer()) { 
            remove();
            return name + " removed from bag";
        } else {
            putInto("3");
            return name + " entered bag";
        }
    }

    protected String getId(){
        return this.id;
    }

    protected String getName(){
        return this.name;
    }

    protected Integer getPic(){
        return this.pic;
    }

    protected Drawable getImg() {
        return this.img;
    }

    protected Compartment getComp(){
        if (this.compId == null){
            return null;
        }
        return Container.inst().getComp(this.compId);
    }

    protected boolean inContainer(){
        return this.compId != null;
    }

    public int compareTo(Object other) throws ClassCastException {
        if (!(other instanceof Item))
        	throw new ClassCastException("An Item object expected.");
        return ((Date) this.created).compareTo((Date) ((Item) other).created);
    }
}
