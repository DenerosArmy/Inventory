package com.denerosarmy.inventory;

import android.annotation.SuppressLint;
import java.util.*;

public class Container{

    private static Container inst = null;
    private Hashtable<String, Compartment> compartmentMap;

    protected Container(){
        compartmentMap = new Hashtable<String, Compartment>();
    }

    protected static Container inst() {
        if (inst == null) {
            inst = new Container();
        }
        return inst;
    }

    protected Compartment getComp(String compId){
        return this.compartmentMap.get(compId);
    }
    
    protected void addComp(Compartment compartment){
        this.compartmentMap.put(compartment.getId(), compartment);
    }

    @SuppressLint("NewApi")
	protected Compartment[] getComps(){
        return Arrays.copyOf(this.compartmentMap.values().toArray(), this.compartmentMap.size(), Compartment[].class);
    }

}
