package com.home.vlas.shops.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class MyLocationListener extends Service implements LocationListener {

    private static final String TAG = "MyLocationListener";
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public double latitude = 0.0;
    public double longitude = 0.0;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private Context context = null;
    private Location location = null;
    private LocationManager locationManager = null;

    public MyLocationListener(Context ctx) {
        Log.v(TAG,
                "MyLocationListener constructor called");
        this.context = ctx;
        getLocationValue();
    }

    public Location getLocationValue() {
        Log.v(TAG, "getLocationValue method called");

        try {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isNetworkEnabled) {

                // Toast.makeText(context, "Net", 1).show();
                Log.v(TAG, "Network provider enabled");

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
                }

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        Log.v(TAG, "Co-ordinates are: " + latitude + " " + longitude);

                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println(location.getLatitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        System.out.println(s);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
