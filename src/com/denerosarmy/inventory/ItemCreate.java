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
   
    public void addItem(View view) {
        EditText nameView = (EditText) findViewById(R.id.itemName);
        String name = nameView.getText().toString();
        createItem(name);
    } 

	public void createItem(String name) {
        Runnable imageLoader = new LoadItemImage(name);
        new Thread(imageLoader).start();

        new Item(name).putInto("3");
        Inventory.getActiveInventory().update();
        finish();
    }

    private class LoadItemImage implements Runnable {
        private String name;

        public LoadItemImage(String name) {
            this.name = name;
        }

        public void run() {
            try {
                String url = "http://" + this.name + ".jpg.to/m";
                String html = getHTML(url);
                System.out.println(html);

                url = getURL(html);

                URL updateURL = new URL(url);
                System.out.println("Loading " + url);
                URLConnection conn = updateURL.openConnection();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                ByteArrayBuffer baf = new ByteArrayBuffer(50);

                int current = 0;
                while((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }

                /* Write the bytes to file. */
                System.out.println("Opening file " + this.name);
                FileOutputStream fos = openFileOutput(this.name, Context.MODE_PRIVATE);
                System.out.println("Opened file");
                fos.write(baf.toByteArray());
                System.out.println("Wrote file");
                fos.close();
                System.out.println("IMAGE LOADED");
                Inventory.getActiveInventory().update();
            } catch (Exception e) {
                System.out.println("IMAGE LOAD ERROR");
                System.out.println(e);
            }
        }

        private String getHTML(String urlToRead) {
            URL url; // The URL to read
            HttpURLConnection conn; // The actual connection to the web page
            BufferedReader rd; // Used to read results from the web page
            String line; // An individual line of the web page HTML
            String result = ""; // A long string containing all the HTML
            try {
                url = new URL(urlToRead);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                rd.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        private String getURL(String text) {
            text = text.substring(text.indexOf("src=\""));
            text = text.substring("src=\"".length());
            text = text.substring(0, text.indexOf("\""));
            System.out.println(text);
            return text;
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

    public static byte[] byteArrayFromDrawable(Drawable d) {
        System.out.println("BYTE ARRAY FROM DRAWABLE");
        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
