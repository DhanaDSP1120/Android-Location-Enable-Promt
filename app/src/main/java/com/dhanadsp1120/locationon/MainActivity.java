package com.dhanadsp1120.locationon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    int PERMISSION_ID = 44;

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (isLocationEnabled()) {

            mFusedLocationClient =LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();


        }
        else
        {
        AlertDialog.Builder buil = new AlertDialog.Builder(this);
        buil.setTitle("Location")
                .setMessage("Please enable the Location to use Tourist Guide")
                .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent ob = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(ob);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(R.drawable.warning);
        AlertDialog m = buil.create();
        m.show();

    }


    }

    public void getLastLocation() {


        if (checkPermissions()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {

                                   String la= Double.toString(location.getLatitude());
                                   String lo=Double.toString(location.getLongitude());


                                    AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                                    b.setMessage(la+"--------"+lo);
                                    Toast.makeText(MainActivity.this,la, Toast.LENGTH_LONG).show();
                                    AlertDialog v=b.create();
                                    v.show();

                                }
                            }
                        }
                );

        } else {
            requestPermissions();
        }
    }
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }
    String lat;
    String lon;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            lat= Double.toString(mLastLocation.getLatitude());
            lon=Double.toString(mLastLocation.getLongitude());



            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            b.setMessage(lat+"--------"+lon);
            Toast.makeText(MainActivity.this,lat, Toast.LENGTH_LONG).show();
            AlertDialog v=b.create();
            v.show();

            Toast.makeText(MainActivity.this,lat, Toast.LENGTH_LONG).show();


        }
    };
    private boolean checkPermissions() { if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

}
