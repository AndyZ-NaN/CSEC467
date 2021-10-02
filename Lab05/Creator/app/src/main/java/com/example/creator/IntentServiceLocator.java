package com.example.creator;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntentServiceLocator extends IntentService {

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GETLOCATION = "com.example.creator.action.getLocation";

    protected final double ERROR_LATITUDE = 404;

    protected final double ERROR_LONGITUDE = 404;

    public IntentServiceLocator() {
        super("IntentServiceLocator");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Log.i("getaction", action);
            Log.i("finaaction", ACTION_GETLOCATION);
            if (ACTION_GETLOCATION.equals(action)) {
                handleAction();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleAction() {
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

        }
        // Send the data instead of a toast containing the using BroadcastManager
        Intent gpsIntent = new Intent("sendCoordinates");
        gpsIntent.putExtra("coordinates", coords);
        Log.i("test2", String.format("Lat: %.4f, Long: %.4f", coords[0], coords[1]));
        sendBroadcast(gpsIntent);
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
}