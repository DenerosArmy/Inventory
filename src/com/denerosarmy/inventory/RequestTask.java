package com.denerosarmy.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.view.Menu;
import android.net.http.AndroidHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import android.view.MenuItem;
import java.io.BufferedInputStream;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.StatusLine;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpEntity;
import java.io.ByteArrayOutputStream;
import 	org.apache.http.client.ClientProtocolException;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.StrictMode;
import android.widget.SearchView.OnQueryTextListener;
import android.view.MenuItem.OnActionExpandListener;
import android.os.AsyncTask;
import 	org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.* ;
class RequestTask extends AsyncTask<String, String, String>{
    public List itemsInBag;
    public RequestTask(List items,String uid) { 
        super();
        itemsInBag = items; 
        itemsInBag.add(new BasicNameValuePair("uid",uid));
    
    }
     public RequestTask() { 
        super();
    
    }
    
    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        HttpPost httppost = new HttpPost("http://192.168.1.102:8000/join/room1");
        HttpGet httpget = new HttpGet("http://192.168.1.102:8000/room/room1");
         
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            httppost.setEntity(new UrlEncodedFormEntity(itemsInBag));
            response = httpclient.execute(httppost);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //Do anything with response..
    }
}

