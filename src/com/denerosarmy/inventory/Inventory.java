package com.denerosarmy.inventory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class Inventory extends Activity{
    //* Called when the activity is first created. 
    //@Override
    //public void onCreate(Bundle savedInstanceState)
    //{
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
    //}

    GridView gridView;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        setContentView(R.layout.main);
     
        gridView = (GridView) findViewById(R.id.gridView1);
        Compartment c1 = new Compartment("1", "Main");
        Compartment c2 = new Compartment("2", "Misc");
        Compartment c3 = new Compartment("3", "Miscer");
        Item i0 = new Item("0", "Pencil");
        Item i1 = new Item("1", "Banana");
        Item i2 = new Item("2", "Popsicle");
        Item i3 = new Item("3", "Pencil");
        Item i4 = new Item("4", "Flour");
        Item i5 = new Item("5", "Hair");
        Item i6 = new Item("6", "Sand");
        Item i7 = new Item("7", "Aluminum Foil");
        Item i8 = new Item("8", "Rubbish");
        Item i9 = new Item("9", "Pencil");

        i0.putInto("1");
        i1.putInto("1");
        i2.putInto("1");
        i3.putInto("1");
        i4.putInto("1");
        i5.putInto("1");
        i6.putInto("2");
        i7.putInto("2");
        i8.putInto("2");
        i9.putInto("3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, Container.inst().getComp("1").getItemList());
     
        gridView.setAdapter(adapter);
     
        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
              int position, long id) {
               Toast.makeText(getApplicationContext(),
              ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });
   
    }

}
