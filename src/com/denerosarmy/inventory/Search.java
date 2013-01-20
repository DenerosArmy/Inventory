package com.denerosarmy.inventory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.app.SearchManager;
import android.view.Menu;
import android.view.MenuItem;

public class Search extends Activity{

    private static final int LOCAL_SEARCH_ID = Menu.FIRST+1;
    private static final int GLOBAL_SEARCH_ID = Menu.FIRST+2;
    ListView container;
    CompartmentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        System.out.println("Called");

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            container = (ListView) findViewById(R.id.compartments);
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println("Query = "+query);
            this.adapter = new SearchCompartmentAdapter(this, query);
            container.setAdapter(this.adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case LOCAL_SEARCH_ID:
                onSearchRequested(); 
                return(true);
            case GLOBAL_SEARCH_ID:
                startSearch(null, false, null, true); 
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, LOCAL_SEARCH_ID, Menu.NONE, "Local Search").setIcon(R.drawable.sample_0);
        menu.add(Menu.NONE, GLOBAL_SEARCH_ID, Menu.NONE, "Global Search").setIcon(R.drawable.sample_1).setAlphabeticShortcut(SearchManager.MENU_KEY);
       
        return(super.onCreateOptionsMenu(menu));
    }

}
