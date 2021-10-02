package com.example.invoker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // From the creator, latitude and longitude are sent back using an intent
            double[] coords = intent.getDoubleArrayExtra("coordinates");
            String coordinates = String.format("Lat: %.4f, Long: %.4f", coords[0], coords[1]);
            Toast.makeText(context, coordinates, Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter("sendCoordinates");
        IntentFilter filter2 = new IntentFilter("com.example.creator.action.getLocation");
        registerReceiver(br, filter);
        setContentView(R.layout.activity_main);

        Intent i = new Intent();
        i.setAction("com.example.creator.action.getLocation");
        i.setComponent(new ComponentName("com.example.creator", "com.example.creator.IntentServiceLocator"));

        startService(i);
    }

    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }
}