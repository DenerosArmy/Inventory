package com.denerosarmy.inventory;

import android.app.Activity;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.animation.Animation;
import java.io.*;
import java.util.Date;

public class Item implements Comparable, Serializable{

    private String id;
    private String name;
    private String filename;
    private String compId;
    public boolean isNew;
    public boolean toBeDeleted;
    private Date created;

    public Item(String name) {
        this(name, name);
    }

    public Item(String id, String name) {
        this.id = id;
        this.name = name;
        this.filename = name;
        this.isNew = true;
        this.toBeDeleted = false;
        this.created = new Date();
        Container.inst().addItem(this);
        Container.inst().save();
    }

    public Item(String id, String name, String filename) {
        this(id, name);
        this.filename = filename;
        Container.inst().save();
    }

    protected void putInto(String compId){
        this.compId = compId;
        Container.inst().getComp(compId).putItem(this);
        Container.inst().save();
    }

    protected void remove(){
        Container.inst().getComp(compId).popItem(this.getId());
        this.compId = null;
        Container.inst().save();
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

    protected int getPic() {
        return 0;
    }

    protected Drawable getImg() {
        byte[] b = new byte[1000000];
        Drawable image = null;

        try {
            System.out.println("Loading image file " + this.filename);
            //FileInputStream fis = new FileInputStream(Inventory.getContext().getFilesDir().getAbsolutePath() + "/" + this.filename);
            FileInputStream fis = Inventory.getContext().openFileInput(this.filename);
            System.out.println("Reading image file");
            fis.read(b);
            System.out.println("Creating drawable from image");
            image =  new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
            System.out.println("IMAGED SUCCESSFULLY LOADED");
        } catch (Exception e) {
            System.out.println("Load image failed");
            e.printStackTrace();
        }
        return image;
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
