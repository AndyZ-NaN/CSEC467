package com.example.creator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.widget.Toast;

public class Wifi extends Service {
    public Wifi() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi = wm.getConnectionInfo();
        String ssid = wi.getSSID();
        // Send the data instead of a toast containing the using BroadcastManager
        Intent ssidIntent = new Intent("ssidBroadcast");
        ssidIntent.putExtra("ssid", ssid);
        sendBroadcast(ssidIntent);

        return Service.START_NOT_STICKY;
        /**
        Intent i = new Intent("get_ssid");
        Toast.makeText(this, ssid, Toast.LENGTH_LONG).show();
        return Service.START_NOT_STICKY; // Service stops running after return
        // String data = intent.getStringExtra("message");
         */
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}