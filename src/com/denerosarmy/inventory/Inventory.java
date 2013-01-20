package com.denerosarmy.inventory;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

public class Inventory extends Activity{

    ListView container;
    NotificationManager notificationManager;
    static boolean initialized;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	System.out.println("Initialized is " + initialized);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	    container = (ListView) findViewById(R.id.compartments);
	    
	    if (!initialized) {

	        Compartment c1 = new Compartment("1", "Drawer");
	        Compartment c2 = new Compartment("2", "Secondary");
	        Compartment c3 = new Compartment("3", "Main");
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
	        i1.putInto("3");
	        i2.putInto("1");
	        i2.remove();
	        i3.putInto("3");
	        i4.putInto("3");
	        i5.putInto("3");
	        i6.putInto("2");
	        i7.putInto("2");
	        i8.putInto("2");
	        i9.putInto("3");
        }
        if (initialized) {
            System.out.println("AWEFHDSIUJUROEWRHUGFOJRATHUGJFAOIRHEGUJAFSIORGHEJ");
		    try {
		    	Intent intent = getIntent();
		    	System.out.println(intent.toString());
		        String id = intent.getStringExtra(ItemCreate.ITEM_ID);
		        String name = intent.getStringExtra(ItemCreate.ITEM_NAME);
		        String comp = intent.getStringExtra(ItemCreate.ITEM_COMPARTMENT);
		        new Item(id, name, R.drawable.olivia_wilde).putInto(comp);
		    } catch (NullPointerException e) {
		    }
        }
        initialized = true;

        container.setAdapter(new CompartmentAdapter(this, Container.inst().getComps()));

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        this.registerReceiver(this.WifiStateChangedReceiver,
                              new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));

    }

    private BroadcastReceiver WifiStateChangedReceiver
        = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
       
            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE ,
                                                    WifiManager.WIFI_STATE_UNKNOWN);
       
            switch(extraWifiState){
                case WifiManager.WIFI_STATE_DISABLED:
                    checkForMissing();
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }
        }
    };

    protected void checkForMissing(){
        for (Item item:Container.inst().getMissingItems()){
            addNotification(item);
        }
    }

    protected void addNotification(Item item){

        Notification noti = new Notification.Builder(this)
                            .setContentTitle("Missing Item")
                            .setContentText("You left your "+item.getName()+" behind!")
                            .setSmallIcon(item.getPic())
                            .build();

        // Hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(Integer.parseInt(item.getId()), noti); 

    }
    
    public void createItem(View view) {
    	Intent intent = new Intent(this, ItemCreate.class);
        startActivity(intent);
    }
}
