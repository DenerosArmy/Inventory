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
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
public class Inventory extends Activity{

    ListView container;
    NotificationManager notificationManager;
    static boolean initialized;
    CompartmentAdapter adapter;
    private static final String TAG = "Inventory"; 
    private String backpackId= null;
    private static final boolean D = true;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    private static final int REQUEST_ENABLE_BT = 3;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast"; 
 
	
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

        this.adapter = new CompartmentAdapter(this);
        container.setAdapter(this.adapter);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        this.registerReceiver(this.WifiStateChangedReceiver,
                              new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
 
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

    public void process(String value){
        runOnUiThread(new Runnable(){
            public void run(){
                this.adapter.notifyDataSetChanged();
            }
        });
    }

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
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) { 
                mChatService = new BluetoothChatService(this,mHandler); 
            }
        }
    }
    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }
    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
    

         
    private final Handler mHandler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
               
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                System.out.println(readMessage); 
                process(readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                backpackId = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + backpackId, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };


}
