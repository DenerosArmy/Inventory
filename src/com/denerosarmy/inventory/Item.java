package com.denerosarmy.inventory;

public class Item{

    private String id;
    private String name;
    private String compId;

    public Item(String id, String name){
        this.id = id;
        this.name = name;
    }

    protected void putInto(String compId){
        this.compId = compId;
        Container.inst().getComp(compId).putItem(this);
    }

    protected void remove(){
        Container.inst().getComp(compId).popItem(this.getId());
        this.compId = null;
    }
    
    protected String getId(){
        return this.id;
    }

    protected String getName(){
        return this.name;
    }

    protected Compartment getComp(){
        if (this.compId == null){
            return null;
        }
        return Container.inst().getComp(this.compId);
    }

}
