package com.denerosarmy.inventory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class Inventory extends Activity{
    //* Called when the activity is first created. 
    //@Override
    //public void onCreate(Bundle savedInstanceState)
    //{
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
    //}

	ListView container;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.main);
     
        container = (ListView) findViewById(R.id.compartments);
        Compartment c1 = new Compartment("1", "Main");
        Compartment c2 = new Compartment("2", "Misc");
        Compartment c3 = new Compartment("3", "Miscer");
        Item i0 = new Item("0", "Pencil", R.drawable.sample_0);
        Item i1 = new Item("1", "Banana", R.drawable.sample_1);
        Item i2 = new Item("2", "Popsicle", R.drawable.sample_2);
        Item i3 = new Item("3", "Pencil", R.drawable.sample_3);
        Item i4 = new Item("4", "Flour", R.drawable.sample_4);
        Item i5 = new Item("5", "Hair", R.drawable.sample_5);
        Item i6 = new Item("6", "Sand", R.drawable.sample_6);
        Item i7 = new Item("7", "Aluminum Foil", R.drawable.sample_7);
        Item i8 = new Item("8", "Rubbish", R.drawable.sample_0);
        Item i9 = new Item("9", "Pencil", R.drawable.sample_3);

        i0.putInto("1");
        i1.putInto("1");
        i2.putInto("1");
        i2.remove();
        i3.putInto("1");
        i4.putInto("1");
        i5.putInto("1");
        i6.putInto("2");
        i7.putInto("2");
        i8.putInto("2");
        i9.putInto("3");

        //ArrayAdapter<String> itemList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Container.inst().getComp("1").getStringItemList());
        //gridView.setAdapter(itemList);
        
        container.setAdapter(new CompartmentAdapter(this, Container.inst().getComps()));
     
    }

}
