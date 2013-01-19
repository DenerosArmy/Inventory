package com.denerosarmy.inventory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.content.Intent;
import android.app.SearchManager;

public class Search extends Activity{

    GridView gridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        System.out.println("Called");

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            gridView = (GridView) findViewById(R.id.itemGrid);
            String query = intent.getStringExtra(SearchManager.QUERY);
            System.out.println("Query = "+query);
            gridView.setAdapter(new ThumbnailAdapter(this, Container.inst().getComp("1").getItemsAndCounts(query)));
        }
    }

}
