package com.denerosarmy.inventory;

import android.app.*;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.http.AndroidHttpClient;
import android.net.wifi.WifiManager;
import android.os.*;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.*;
import android.view.MenuItem.OnActionExpandListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.SearchView.OnQueryTextListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.DefaultHttpClient;


public class Inventory extends Activity{
    CompartmentAdapter adapter;
    ListView container;
    NotificationManager notificationManager;
    public boolean testImgUp = false;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private Hashtable<String,Item> rfidTags;
    private SearchView searchView;
    private String backpackId= null;
    private String rfidTag = "";
    private int bufferLen = 0;
    private int byteCount = 0;
    private int messageState;
    private static Context context;
    private static Inventory inventory;
    public Compartment compartment; 
    public Compartment nullspace;
    static boolean initialized;
    
    private final String ANDROID_ID = Secure.ANDROID_ID;
    private static final String TAG = "Inventory"; 
    private static final boolean D = true;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_ENABLE_BT = 3;
    public static final String BASEURL = "http://192.168.1.102:3000";
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast"; 
    public static final char HEADER1 = '7';
    public static final char HEADER2 = 'C';
    public static final int HEADER1WAIT = 0;
    public static final int HEADER2WAIT = 1;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    public static final int READING_TAG = 2; 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
       
        System.out.println("Initialized is " + initialized);
        Inventory.context = getApplicationContext();
        Inventory.inventory = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        container = (ListView) findViewById(R.id.compartments);
        
        if (!initialized){
            rfidTags = new Hashtable<String,Item>();
            addTestItems();
        }

        initialized = true;

        this.adapter = new CompartmentAdapter(this);
        container.setAdapter(this.adapter);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        this.registerReceiver(this.WifiStateChangedReceiver,
                              new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        if (!mBluetoothAdapter.isEnabled()) {
            System.out.println("Enabling bluetooth");
            mBluetoothAdapter.enable();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        checkContextAware();
    }

    public static Inventory getActiveInventory() {
        return Inventory.inventory;
    }

    public static Context getContext(){
        return Inventory.context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_item:
                createItem();
                return true;
            case R.id.pair_device:
                deviceList();
                return true;
            case R.id.join_room:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Join a Party!");

                alert.setMessage("Party Name:");

                // Set an EditText view to get user input 
                final EditText input = new EditText(this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                   String value = input.getText().toString();
                   new RequestTask(compartment.itemMapList, ANDROID_ID).execute("");
                    }     
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                  }
                });
                alert.show();
                return true;
            case R.id.test_animation:
                if (this.testImgUp) {
                    compartment.getItem("8008").scheduleDeletion();
                    this.testImgUp = false;
                    update();
                } else {
                    Drawable d = getResources().getDrawable(R.drawable.olivia_wilde);
                    saveDrawable("Female", d);
                    Item i= new Item("8008", "Female");
                    i.putInto("3");
                    update();
                    this.testImgUp = true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.options, menu);
        
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        //this.searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText){
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query){
                container.setAdapter(new SearchCompartmentAdapter(Inventory.this, query));
                update();
                return true;
            }
        });
        MenuItem searchBar = menu.findItem(R.id.menu_search);
        searchBar.setOnActionExpandListener(new OnActionExpandListener(){

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item){
                container.setAdapter(Inventory.this.adapter);
                update();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item){
                return true;
            }

        });
        //searchView.setOnCloseListener(new OnCloseListener(){
            //@Override
            //public boolean onClose(){
                //container.setAdapter(Inventory.this.adapter);
                //update();
                //return true;
            //}
        //});
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
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
                    //checkContextAware();
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

    protected void stateToggle(String rfidTag) { 
        System.out.println(rfidTag);


        
        if (rfidTags.containsKey(rfidTag)) { 
            System.out.println("BOO");
            Item the_item = rfidTags.get(rfidTag); 
            Toast.makeText(this, the_item.flip(),Toast.LENGTH_SHORT).show(); 
        }


    }

    protected void process(String message){
        rfidTag += message.replace('\n',' ').trim();
        System.out.println("MESSAGE IS " + message);

        if (rfidTag.length() >= 2){ 
            if (rfidTag.substring(rfidTag.length()-2).equals("25")) { 
                    if (!rfidTag.substring(0,rfidTag.length()-2).equals("")) { 
                        String tag = rfidTag.substring(0,rfidTag.length());
                        stateToggle(tag);
                               
                    }
                    rfidTag = "";
                }

            }

    }

    protected void update(){
        runOnUiThread(new Runnable(){
            public void run(){
                _update();
            }
        });
    }

    protected void _update(){
        this.adapter.notifyDataSetChanged();
    }

    protected void checkForMissing(){
        for (Item item:Container.inst().getMissingItems()){
            addMissingNotification(item);
        }
    }

    protected void checkContextAware(){
        // Weather
        //boolean cold = false;
        //try{
            //URL url = new URL("http://weather.yahooapis.com/forecastrss?w=2347597&u=c");
            //try{
                //InputStream is = url.openConnection().getInputStream();
                //BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );
                //String line = null;
                //while ((line = reader.readLine()) != null){
                    //if (line.substring(0, 18).equals("<yweather:condition")){
                        //if (Integer.parseInt(line.substring(47, 48)) < 15){
                            //cold = true;
                            //break;
                        //}
                    //}
                    //System.out.println(line);
                //}
                //reader.close();
            //}catch (IOException e){
            //}
        //}catch (MalformedURLException e){
        //}
        boolean cold = true;
        System.out.println("Context-aware check");
        if (cold){
        //if ((cold)&&(Container.inst().getItemNamed("jacket") == null)){
            System.out.println("It's cold!");
            Notification noti = new Notification.Builder(this)
                                .setContentTitle("It's cold outside")
                                .setContentText("Don't forget your jacket!")
                                .setSmallIcon(R.drawable.cold)
                                .build();
            // Hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(874520642, noti); 
        }
    }
  
    protected void addMissingNotification(Item item){

        Notification noti = new Notification.Builder(this)
                            .setContentTitle("Missing Item")
                            .setContentText("You left your "+item.getName()+" behind!")
                            .setSmallIcon(item.getPic())
                            .build();

        // Hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(Integer.parseInt(item.getId()), noti); 

    } 

    public void deviceList(View view) {
      deviceList();
    }

    public void deviceList() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
         
    }

    public void createItem(View view) {
        createItem();
    }

    public void createItem() {
        Intent intent = new Intent(this, ItemCreate.class);
        startActivity(intent);
    }
    
    public void genItem(View view) {
    	process("|1abcd");
        /*Item newItem = new Item("9999", "Test", R.drawable.olivia_wilde);
        newItem.putInto("3");
        update();*/
    	update();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (mBluetoothAdapter.isEnabled()) {
        // Otherwise, setup the chat session
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
    try{
        Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
        // Save the items
        Container.inst().save();
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
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                    Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_LONG).show();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                    Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_LONG).show();
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
                String readMessage = new String(readBuf,0,msg.arg1);
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

 public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"ON ACTIVITY RESULT");
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns wsith a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, false);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
                if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
              // Start the Bluetooth chat services
                mChatService.start();
            }

            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "BT not enabled");
                finish();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    private void saveDrawable(String name, Drawable d) {
        String path = getContext().getFilesDir().getAbsolutePath() + "/" + name;
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        try {
            FileOutputStream fos = openFileOutput(name, Context.MODE_PRIVATE);
            fos.write(bitmapdata);                                   
            fos.close();                                                    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addTestItems() {
        Compartment c3 = new Compartment("3", "My stuff");
        compartment = c3;

        Drawable d0 = getResources().getDrawable(R.drawable.sample_0);
        Drawable d1 = getResources().getDrawable(R.drawable.sample_1);
        Drawable d2 = getResources().getDrawable(R.drawable.sample_2);
        Drawable d3 = getResources().getDrawable(R.drawable.sample_3);
        Drawable d4 = getResources().getDrawable(R.drawable.sample_4);
        Drawable d5 = getResources().getDrawable(R.drawable.sample_5);
        Drawable d6 = getResources().getDrawable(R.drawable.sample_6);
        Drawable d7 = getResources().getDrawable(R.drawable.sample_7);
        Drawable d10 = getResources().getDrawable(R.drawable.sample_7);
        Drawable d11 = getResources().getDrawable(R.drawable.sample_7);

        Item i0 = new Item("0", "Earbuds");
        saveDrawable("Earbuds", d0);

        Item i1 = new Item("1", "Glasses");
        saveDrawable("Glasses", d1);

        Item i2 = new Item("2", "Headphones");
        saveDrawable("Headphones", d2);

        Item i3 = new Item("3", "Jacket");
        saveDrawable("Jacket", d3);

        Item i5 = new Item("5", "Mouse");
        saveDrawable("Mouse", d5);

        Item i6 = new Item("6", "Passport");
        saveDrawable("Passport", d6);

        Item i7 = new Item("7", "Pencil");
        Item i10 = new Item("10", "Pencil");
        Item i11 = new Item("11", "Pencil");
        saveDrawable("Pencil", d7);

        Item i8 = new Item("8", "Nexus");
        saveDrawable("Nexus", d10);
        Item i9 = new Item("9", "Multimeter");
        saveDrawable("Multimeter", d11);

        i0.putInto("3");
        i1.putInto("3");
        i2.putInto("3");
        i5.putInto("3");
        //i6.putInto("3");
        i7.putInto("3");
        i10.putInto("3");
        i11.putInto("3");
        //i8.putInto("3");

        rfidTags.put("363730303732363437343035DA325",i8);

        rfidTags.put("102343530304637324434304446DA3",i9);
        rfidTags.put("102343530304238453546384530DA3",i3);
        rfidTags.put("102343530304238453546454536DA3",i6);

        Drawable o = getResources().getDrawable(R.drawable.olivia_wilde);
        saveDrawable("Olivia", o);
    }
}
