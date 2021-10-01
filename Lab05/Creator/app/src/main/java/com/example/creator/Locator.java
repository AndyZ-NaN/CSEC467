package com.example.creator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class Locator extends Service {
    protected final double ERROR_LATITUDE = 404;
    protected final double ERROR_LONGITUDE = 404;
    public Locator() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        double[] coords = new double[2];
        Log.i("test", String.format("Lat: %.4f, Long: %.4f", coords[0], coords[1]));
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                coords = getLocation(lm);
            } catch (NullPointerException n) {
                coords[0] = ERROR_LATITUDE;
                coords[1] = ERROR_LONGITUDE;
                Log.i("test1", String.format("Lat: %.4f, Long: %.4f", coords[0], coords[1]));
            }
        } else {
            coords[0] = ERROR_LATITUDE;
            coords[1] = ERROR_LONGITUDE;
            Log.i("test2", String.format("Lat: %.4f, Long: %.4f", coords[0], coords[1]));
        }
        // Send the data instead of a toast containing the using BroadcastManager
        Intent gpsIntent = new Intent("sendCoordinates");
        gpsIntent.putExtra("coordinates", coords);
        sendBroadcast(gpsIntent);

        return Service.START_NOT_STICKY;
        /**
         Intent i = new Intent("get_ssid");
         Toast.makeText(this, ssid, Toast.LENGTH_LONG).show();
         return Service.START_NOT_STICKY; // Service stops running after return
         // String data = intent.getStringExtra("message");
         */
    }

    private double[] getLocation(LocationManager lm) {
        if (ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "GPS permissions not granted", Toast.LENGTH_SHORT).show();
        } else {
            LocationListener ls = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {

                }
            };
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ls);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double[] coordinates = {location.getLatitude(), location.getLongitude()};
                return coordinates;
            }
        }
            return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}