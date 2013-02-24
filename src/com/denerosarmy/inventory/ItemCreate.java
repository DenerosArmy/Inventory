package com.denerosarmy.inventory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.apache.http.util.ByteArrayBuffer;

import java.io.*;
import java.net.*;


/**
 * Activity which displays a form to the user
 */
public class ItemCreate extends Activity {
	
    public final static String ITEM_NAME = "com.denerosarmy.inventory.ITEM_NAME";
    public final static String ITEM_ID = "com.denerosarmy.inventory.ITEM_ID";
    public final static String ITEM_COMPARTMENT = "com.denerosarmy.inventory.ITEM_COMPARTMENT";
    public final static String ITEM_IMAGE = "com.denerosarmy.inventory.ITEM_IMAGE";

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
        //Intent intent = new Intent(this, Inventory.class);
        EditText nameView = (EditText) findViewById(R.id.itemName);
        String name = nameView.getText().toString();
        //intent.putExtra(ITEM_NAME, name);
        //intent.putExtra(ITEM_ID, "12345654");
        //intent.putExtra(ITEM_COMPARTMENT, "3");

        // Image grabbing
        //Runnable imageLoader = new LoadItemImage(name);
        //new Thread(imageLoader).start();
        new Item(name).putInto("3");
        Inventory.getActiveInventory().update();
        finish();
        //startActivity(intent);
    }

    private class LoadItemImage implements Runnable {
        private String name;

        public LoadItemImage(String name) {
            this.name = name;
        }

        public void run() {
            try {
                String url = "http://" + this.name + ".jpg.to";
                //URL updateURL = new URL("http://penis.jpg.to");
                URL updateURL = new URL(url);
                URLConnection conn = updateURL.openConnection();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(50);

                int current = 0;
                while((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }

                /* Write the bytes to file. */
                System.out.println("Opening file");
                FileOutputStream fos = openFileOutput(this.name, Context.MODE_PRIVATE);
                System.out.println("Opened file");
                fos.write(baf.toByteArray());
                System.out.println("Wrote file");
                fos.close();
                System.out.println("IMAGE LOADED");
            } catch (Exception e) {
                System.out.println("IMAGE LOAD ERROR");
                System.out.println(e);
            }
        }
    };

    public static Drawable drawableFromURL(String url) {
        System.out.println("DRAWABLE FROM URL");
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            System.out.println("Successful image grab from URL");
            return d;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    //public static Drawable drawableFromURL(String url) throws IOException {
    //    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    //    System.out.println("ATTEMPTING TO CONNECT");
    //    connection.connect();
    //    System.out.println("CONNECTION ESTABLISHED");
    //    InputStream input = connection.getInputStream();

    //    Bitmap x = BitmapFactory.decodeStream(input);
    //    return new BitmapDrawable(x);
    //}

    public static byte[] byteArrayFromDrawable(Drawable d) {
        System.out.println("BYTE ARRAY FROM DRAWABLE");
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
