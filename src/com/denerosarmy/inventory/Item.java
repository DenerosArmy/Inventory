package com.denerosarmy.inventory;

public class Item{

    private String id;
    private String name;
    private String compId;
    private Integer pic;

    public Item(String id, String name, Integer pic){
        this.id = id;
        this.name = name;
        this.pic = pic;
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
    
    protected void flip() { 
        System.out.println("FLIP CALLED FLIP CALLED");
        if (inContainer()) { 
            remove(); 
        } else {
            putInto("1");
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

    protected Compartment getComp(){
        if (this.compId == null){
            return null;
        }
        return Container.inst().getComp(this.compId);
    }

    protected boolean inContainer(){
        return this.compId != null;
    }

}
