package muzimuzi.jejuhackerton.com.muzimuzi.util;

/*
 *
 *  * *
 *  *  * Created by SeungEui Hong on 18. 5. 12 오후 10:15
 *  *  *
 *  *  * Copyright (C) SeungEui Hong - All Rights Reserved
 *  *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  *  * Proprietary and confidential
 *  *  *
 *  *
 *
 */


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class MyLocationManager {
    private Context context;
    private Location lastKnownLocation;
    private double myLocationLng;
    private double myLocationLat;
    private GoogleMap googleMap;
    private boolean firstCheck;

    public boolean isFirstCheck() {
        return firstCheck;
    }

    public void setFirstCheck(boolean firstCheck) {
        this.firstCheck = firstCheck;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public double getMyLocationLng() {
        return myLocationLng;
    }

    public void setMyLocationLng(double myLocationLng) {
        this.myLocationLng = myLocationLng;
    }

    public double getMyLocationLat() {
        return myLocationLat;
    }

    public void setMyLocationLat(double myLocationLat) {
        this.myLocationLat = myLocationLat;
    }

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public void setLocationListener(LocationListener locationListener) {
        this.locationListener = locationListener;
    }

    public GoogleMap getGoogleMap() {

        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    private LocationListener locationListener;

    public MyLocationManager(final Context context, final GoogleMap googleMap) {
        this.context = context;
        this.lastKnownLocation = null;
        this.myLocationLng = 0;
        this.myLocationLat = 0;
        this.firstCheck = false;
        this.googleMap = googleMap;
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

                lastKnownLocation = location;

                if (myLocationLat == 0 && myLocationLng == 0)
                    firstCheck = true;

                myLocationLat = lastKnownLocation.getLatitude();
                myLocationLng = lastKnownLocation.getLongitude();

                if (firstCheck && googleMap != null) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocationLat, myLocationLng), 16));
                    firstCheck = false;
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
    }

    public void requestLocationListener()  {

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    public void getLastLocation() {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = lm.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            myLocationLng = lastKnownLocation.getLongitude();
            myLocationLat = lastKnownLocation.getLatitude();
        }
    }

}
