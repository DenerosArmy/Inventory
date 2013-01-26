package com.denerosarmy.inventory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


/**
 * Activity which displays a form to the user
 */
public class ItemCreate extends Activity {
	
    public final static String ITEM_NAME = "com.denerosarmy.inventory.ITEM_NAME";
    public final static String ITEM_ID = "com.denerosarmy.inventory.ITEM_ID";
    public final static String ITEM_COMPARTMENT = "com.denerosarmy.inventory.ITEM_COMPARTMENT";

  @SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_create);
	}
    
    public void goBack(View view) {
    	Intent intent = new Intent(this, Inventory.class);
        startActivity(intent);
    }

	public void addItem(View view) {
    	Intent intent = new Intent(this, Inventory.class);
    	EditText nameView = (EditText) findViewById(R.id.itemName);
    	String name = nameView.getText().toString();
    	intent.putExtra(ITEM_NAME, name);

    	EditText idView = (EditText) findViewById(R.id.rfid);
    	String id = idView.getText().toString();
    	intent.putExtra(ITEM_ID, id);
    	
    	EditText compView = (EditText) findViewById(R.id.compartment);
    	String comp = compView.getText().toString();
    	intent.putExtra(ITEM_COMPARTMENT, comp);

        startActivity(intent);
	}

}
