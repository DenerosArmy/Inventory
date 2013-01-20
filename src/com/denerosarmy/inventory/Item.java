package com.denerosarmy.inventory;

import java.util.Date;

import android.view.animation.Animation;



public class Item implements Comparable {

    private String id;
    private String name;
    private String compId;
    private Integer pic;
    public boolean isNew;
    public boolean toBeDeleted;
    private Date created;

    public Item(String id, String name, Integer pic){
        this.id = id;
        this.name = name;
        this.pic = pic;
        this.isNew = true;
        this.toBeDeleted = false;
        this.created = new Date();
        Container.inst().addItem(this);
    }

    protected void putInto(String compId){
        this.compId = compId;
        Container.inst().getComp(compId).putItem(this);
    }

    protected void remove(){
        Container.inst().getComp(compId).popItem(this.getId());
        this.compId = null;
    }
    
    protected void scheduleDeletion() {
    	this.toBeDeleted = true;
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
