package com.example.martinlamby.parking;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

//Implements all functions for getting the current location


public class GeoLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final double ERROR_LOCATION_VALUE = 300;
    private static GoogleApiClient mGoogleApiClient;
    private static Location lastLocation;

    @Override
    public void onCreate() {
        //init and connect Google Api Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        super.onCreate();
    }

    //gets the current User location Longitude if possible
    public static double getLastLocationLatitude() {
        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return lastLocation.getLatitude();
        } catch (Exception e) {
            return ERROR_LOCATION_VALUE;
        }
    }

    //gets the current User location Longitude if possible
    public static double getLastLocationLongitude() {
        try {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            return lastLocation.getLongitude();
        } catch (Exception e) {
            return ERROR_LOCATION_VALUE;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("GeoLocationService started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        System.out.println("GeoLocationService stopped");
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("GeoLocationService has connected successfully");
    }


    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("GeoLocationService connection has been suspended   " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("GeoLocationService connection Failed----ErrorResult:" + connectionResult.getErrorCode());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not implemented");
    }
}
