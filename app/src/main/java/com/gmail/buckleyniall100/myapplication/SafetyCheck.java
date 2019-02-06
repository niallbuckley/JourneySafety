package com.gmail.buckleyniall100.myapplication;

import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SafetyCheck {
    //ArrayList<Integer> driverSpeeds = new ArrayList<Integer>();
    int maxSpeed = 0;
    public SafetyCheck(int driverSpeed, TextView data, LatLng current, jsonData jsonFile) {
        //String stringSpeedLimit =Integer.parseInt
        String speedLimit = String.valueOf(data.getText());
        //String speedLimit = "50";
        try {
            maxSpeed = Integer.parseInt(speedLimit);
        } catch (NumberFormatException ex) {
            System.out.println("Not an int!");
        }
        //could do some kind of if the average of the last three readings is greater than speed limit
        System.out.println("speedLimit: " + speedLimit);
        System.out.println("speedLimit: " + maxSpeed);
        System.out.println("driverSpeed: " + driverSpeed);
        //if(maxSpeed < driverSpeed)
        if(maxSpeed > -1){
            System.out.println("REAL ADDRESS " + fetchData.getAddress());
            jsonFile.writeToJson(fetchData.getAddress());
        }
        //jsonFile.getObj();
        System.out.println(jsonFile.getObj());
    }

    //}

}
