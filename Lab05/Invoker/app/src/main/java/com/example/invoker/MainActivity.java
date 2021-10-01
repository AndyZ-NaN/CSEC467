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

            double[] coords = intent.getDoubleArrayExtra("coordinates");
            String coordinates = String.format("Lat: %.4f, Long: %.4f", coords[0], coords[1]);
            //Log.i("test", coordinates);
            Toast.makeText(context, coordinates, Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter("sendCoordinates");
        registerReceiver(br, filter);
        setContentView(R.layout.activity_main);

        Intent i = new Intent();
        i.setComponent(new ComponentName("com.example.creator", "com.example.creator.Locator"));

        startService(i);
    }

    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }
}