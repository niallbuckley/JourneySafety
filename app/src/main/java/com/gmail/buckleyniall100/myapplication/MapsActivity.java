package com.gmail.buckleyniall100.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    String charset = "UTF-8";
    public static TextView data;
    public static Button startButton;
    public static Button finishButton;
    boolean startButtonPressed = false;
    boolean finishButtonPressed = false;
    //private FirebaseUser mCurrentUser;
    //private userId = firebase.auth().currentUser.uid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase = firebaseDatabase.getReference("users");
    //String email = user.getEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final jsonData jsonFile = new jsonData();

        data =  (TextView) findViewById(R.id.title);
        startButton = (Button) findViewById(R.id.start);
        finishButton = (Button) findViewById(R.id.finish);
        finishButton.setVisibility(View.INVISIBLE);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }

        //check if network provider is enabled
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                System.out.println("Long is:" + longitude + " and Lat is:" + latitude);
                LatLng current = new LatLng(latitude, longitude);
                if (marker != null) {
                    marker.remove();
                    marker = mMap.addMarker(new MarkerOptions().position(current).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                } else {
                    marker = mMap.addMarker(new MarkerOptions().position(current).title("Your Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));
                }
                if (startButtonPressed && !finishButtonPressed) {
                    int speed = (int) (location.getSpeed());
                    fetchData process = new fetchData(latitude, longitude);
                    process.execute();
                    new SafetyCheck(speed, data, current, jsonFile);
                }
                else if(finishButtonPressed){
                    System.out.println(jsonFile.getObj());
                    JSONObject p = jsonFile.getObj();

                    try {
                        JSONArray array = p.getJSONArray("Error");
                        LatLng object = (LatLng) array.get(1);
                        System.out.println("LAT: " + object.latitude);
                        Date loudScreaming = (Date) p.get("Date/ Time");
                        System.out.println(loudScreaming);
                        /*mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                        Map<String, JourneyDetails> users = new HashMap<>();
                        users.put(String.valueOf(user), new JourneyDetails(1, loudScreaming));*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //String userId = "123456";
                    //JourneyDetails c  = new JourneyDetails(1, "Hiya");
                    //mDatabase.child("users").setValue(c);
                    mDatabase.setValue("Hello, World!");
                }
                //mMap.addMarker(new MarkerOptions().position(latLng).title("current loc"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
        }




    }

    public void finishButtonClicked(View v){
        finishButtonPressed = true;
        finishButton.setVisibility(View.INVISIBLE);
    }

    public void startButtonClicked(View v){
        startButton.setVisibility(View.INVISIBLE);
        startButtonPressed = true;
        finishButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}