package com.example.tonycurrie.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap Map;
    private GoogleApiClient GoogleApiClient;
    private LocationRequest LocationRequest;
    Marker marker;
    LatLng latLng;
    Float lat,longi;
    private LocationManager LocationMngr;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    RowItem rlist;
    String productName;
    SharedPreferences position;
    String pos;
    String barcode_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtaining the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //initialising locationmanager which help dealing with user's current ocation
        LocationMngr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        productName="Boys_Hoodie";
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Map = googleMap;
        Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Map.getUiSettings().setZoomControlsEnabled(true);
        //Calling method to obtain user's current location
        CurrentLocation();
        //Calling method to display nearby locations on map
        ShopLocation();
    }

    private void ShopLocation() {

        try {
            //Fetching the barcode id of the product
            barcode_id=BarcodeActivity.barcode_id;
            //Fetching the database
            openHelper = new DatabaseHelper(this);
            database = openHelper.getWritableDatabase();
            //Fetching details of the product from database
            Cursor cursor = database.rawQuery("SELECT * FROM BARCODE_PRODUCT WHERE BARCODE_NO = '" + barcode_id + "'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //Fetching latitude and longitude of particular shop
                lat = Float.parseFloat(cursor.getString(5));
                longi = Float.parseFloat(cursor.getString(4));
                //shop name
                String shop = cursor.getString(2);
                latLng = new LatLng(lat, longi);
                Map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                //Placing the marker at shop location
                Marker markershop = Map.addMarker(new MarkerOptions().position(latLng).title(shop));
                markershop.showInfoWindow();
                //Moving the camera where the marker is placed
                Map.animateCamera(CameraUpdateFactory.zoomTo(11));
                cursor.moveToNext();
            }
            cursor.close();
            this.database.close();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //Current location
    public void CurrentLocation() {
        //Checking if permission to access the location is granted or not
        if (ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(LocationActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            Map.setMyLocationEnabled(true);
            LocationMngr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new android.location.LocationListener() {
               //Fetching user's current location on location change
                @Override
                public void onLocationChanged(Location location) {
                    LatLng latLngshop = new LatLng(location.getLatitude(), location.getLongitude());
                    Map.moveCamera(CameraUpdateFactory.newLatLng(latLngshop));
                    marker = Map.addMarker(new MarkerOptions().position(latLngshop).title("Your Location!!"));
                    marker.showInfoWindow();
                    Map.animateCamera(CameraUpdateFactory.zoomTo(11));

                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            CurrentLocation();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        GoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        //stop location updates
        if (GoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(GoogleApiClient,this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest = new LocationRequest();
        LocationRequest.setInterval(1000 * 30);
        LocationRequest.setFastestInterval(1000 * 30);
        LocationRequest.setSmallestDisplacement(0.25f);
        LocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(GoogleApiClient, LocationRequest, this);
        }


    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Verifying if GPS is enabled or not, if not prompting user to turn it on
        if (!LocationMngr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS not Enabled").setMessage("Would you like to enable GPS ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}