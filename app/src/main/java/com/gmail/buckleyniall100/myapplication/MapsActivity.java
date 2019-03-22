package com.gmail.buckleyniall100.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
    public static TextView predictedData;
    public static Button startButton;
    public static Button finishButton;
    boolean startButtonPressed = false;
    boolean finishButtonPressed = false;
    private FirebaseUser mCurrentUser;
    //private userId = firebase.auth().currentUser.uid;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int ticked;
    private int hoursSlept;
    private int hoursSinceSlept;
    //private DatabaseReference mDatabase = firebaseDatabase.getReference("users");

    //String email = user.getEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        predictedData = (TextView) findViewById(R.id.predicted_score);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ticked = extras.getInt("numTicked");
            hoursSlept = extras.getInt("hoursSlept");
            hoursSinceSlept = extras.getInt("hoursSinceSlept");
            //The key argument here must match that used in the other activity
        }
        System.out.println("Ticked: " + ticked);
        System.out.println("Hours Slept: " + hoursSlept);
        System.out.println("Hours since sleep: " + hoursSinceSlept);
        //double ps = new predictScore().predictScore(ticked, hoursSlept, hoursSinceSlept);
        predictScore ps = new predictScore(1, 2, 3);
        ps.setUpData();

        System.out.println("User ID: " + user.getUid());
        //final String userId = user.getUid();
        final String userId = "userTest";
        final jsonData jsonFile = new jsonData();
        jsonFile.writeMetaToJson(ticked, hoursSlept, hoursSinceSlept);


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
                else if(finishButtonPressed) {
                    int overAllScore = 0;
                    DatabaseReference myRef = database.getReference("users/" + userId);
                    Date finishDate = new Date();
                    JSONObject p = jsonFile.getObj();
                    locationManager.removeUpdates(locationListener);
                    locationManager = null;
                    /*try {
                        p.put("End Date/ Time", date);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("JSON file final= " + p);*/
                    try {
                        JSONArray array = p.getJSONArray("Error");
                        int numMistakes = array.length();
                        Date startDate = (Date) p.get("Date/ Time");
                        int ticked = (int) p.get("numTicked");
                        int hoursSlept = (int) p.get("hoursSlept");
                        int hoursSinceSlept = (int) p.get("hoursSinceSlept");
                        overAllScore = calculateScore(numMistakes, startDate, finishDate);
                        User user = new User(overAllScore, startDate, finishDate, ticked, hoursSlept, hoursSinceSlept);
                        System.out.println("Hello??");
                        myRef.push().setValue(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    finishButtonPressed = false;
                    startButtonPressed = false;

                    journeyFinished(p, overAllScore);
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

    private void journeyFinished(JSONObject p, int allScore) {
        System.out.println("YELLOWW "  + p);
        System.out.println("Predict Score " + predictedData.getText().toString());
        Intent intent = new Intent(MapsActivity.this, fileDownload.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("journeyDetails", p.toString());
        intent.putExtra("predictedScore", predictedData.getText().toString());
        intent.putExtra("journeyScore", String.valueOf(allScore));
        startActivity(intent);
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

    private int calculateScore(int errors, Date startDate, Date endDate){
        if(errors == 0){
            return 10;
        }
        double diffInMins = (double)( (endDate.getTime() - startDate.getTime())
                / (1000 * 60) );
        if( errors / (diffInMins/60) > 0 &&  errors / ((diffInMins/60)) <= 1 ){
            return 9;
        }
        else if ( errors / (diffInMins/60) > 1 &&  errors / ((diffInMins/60)) <= 2 ){
            return 8;
        }
        else if ( errors / (diffInMins/60) > 2 &&  errors / ((diffInMins/60)) <= 3 ){
            return 7;
        }
        else if ( errors / (diffInMins/60) > 3 &&  errors / ((diffInMins/60)) <= 4 ){
            return 6;
        }
        else if ( errors / (diffInMins/60) > 4 &&  errors / ((diffInMins/60)) <= 5 ){
            return 5;
        }
        else if ( errors / (diffInMins/60) > 5 &&  errors / ((diffInMins/60)) <= 6 ){
            return 4;
        }
        else if ( errors / (diffInMins/60) > 6 &&  errors / ((diffInMins/60)) <= 7 ){
            return 3;
        }
        else if ( errors / (diffInMins/60) > 7 &&  errors / ((diffInMins/60)) <= 8 ){
            return 2;
        }
        else if (((diffInMins/60)) <= 7 ){
            return 1;
        }
        return 0;
    }

    public TextView getPredictedData(){
        return predictedData;
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