package com.gmail.buckleyniall100.myapplication;

import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SafetyCheck {
    private String previous;
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
        // + " ---- Reason: Speeding"
        System.out.println("fetch1 "+fetchData.getAddress());

        JSONObject p = jsonFile.getObj();
        JSONArray array = null;
        try {
            array = p.getJSONArray("Error");
            if (array != null){
                int numMistakes = array.length();
                previous = String.valueOf(array.get(numMistakes-1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("prev1 "+ previous);
        System.out.println("prev2 " + fetchData.getAddress()+ " ---- Reason: Speeding");
        //may not be necessary.
        if (maxSpeed > -1) {
                if (previous == null || !previous.equals(fetchData.getAddress() + " ---- Reason: Speeding")) {
                    System.out.println("ok");
                    if(fetchData.getAddress() != null){
                        jsonFile.writeToJson(fetchData.getAddress() + " ---- Reason: Speeding");
                    }
                }
                //intersection shite.
                //previous = fetchData.getAddress();
        }
        System.out.println("nok");
        //jsonFile.getObj();
        System.out.println(jsonFile.getObj());
    }

    //}

}
