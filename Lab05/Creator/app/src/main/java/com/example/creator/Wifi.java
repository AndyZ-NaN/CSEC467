package com.example.creator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class Wifi extends Service {
    public Wifi() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data = intent.getStringExtra("message");
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
        return Service.START_NOT_STICKY; // Service stops running after return
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}