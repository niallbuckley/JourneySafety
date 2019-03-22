package com.gmail.buckleyniall100.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class predictScore {
    static double[][][] TRAINING_DATA;
    static LinearRegression lr;
    static double ticked, hourSlept, hoursSinceSlept;
    String userId = "userTest";
    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users");
    private int count=0;
    private boolean first = true;
    Context context;
    //private double[][][] TRAINING_DATA2;
    Double[][][] TRAINING_DATA2 = new Double[10][2][4];
    private double predicted;

    public predictScore( int ticked, int hoursSlept, int hoursSinceSlept) {
        this.ticked = ticked;
        this.hoursSinceSlept = hoursSinceSlept;
        this.hourSlept = hoursSlept;
    }
    //static Double[][][] TRAINING_DATA2;

    protected void setUpData() {
        Query recentPostsQuery = db.child(userId).orderByKey().limitToLast(10);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("GOTHERE");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println("Ordered?? " + child.getValue());
                    JSONObject val = new JSONObject((Map) child.getValue());
                    try {
                        TRAINING_DATA2[count][0][0] = Double.valueOf(1);
                        TRAINING_DATA2[count][0][1] = Double.valueOf((Long) val.get("hoursSlept"));
                        TRAINING_DATA2[count][0][2] = Double.valueOf((Long) val.get("hoursSinceSlept"));
                        TRAINING_DATA2[count][0][3] = Double.valueOf((Long) val.get("ticked"));
                        TRAINING_DATA2[count][1][0] = Double.valueOf((Long) val.get("score"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("TD: " + TRAINING_DATA2[0][0]);
                    System.out.println("TD: " + TRAINING_DATA2[0]);
                    System.out.println("TD: " + TRAINING_DATA2[count][0][0]);
                    count++;
                }
                predictScore1(TRAINING_DATA2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Real Error");

            }
        });
    }

    @SuppressLint("NewApi")
    private double predictScore1(Double[][][] Testing) {
        System.out.println("NULL?? " + Testing[1][0][2]);
        double[][] xArray = new double[Testing.length][Testing[0][0].length];
        double[][] yArray = new double[Testing.length][1];
        System.out.println(xArray.length);
        System.out.println(Testing.length);
        System.out.println("Check == " + Testing[0][0].length);
        for (int i=0; i< 9; i++){
            for(int j= 0; j<4; j++){
                xArray[i][j] = Testing[i][0][j];
                yArray[i][0] = Testing[i][1][0];
            }
        }
        try {
            lr = new LinearRegression(xArray, yArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ticked= "+ticked + " "+hourSlept + " " + hoursSinceSlept);
        predicted = lr.estimateRent(hourSlept, hoursSinceSlept, ticked);
        System.out.println("Estimated rent: " + predicted);
        MapsActivity.predictedData.append(String.valueOf((int) this.predicted));
        //MapsActivity obj = new MapsActivity();
        //TextView tv = obj.getPredictedData();
        //tv.setText("Hello");
        //MapsActivity.predictedData.setText((int) predicted);
        //TextView predictedData = (TextView) ((Activity)context).findViewById(R.id.predicted_score);
        //predictedData.setText("Hi");
        //MapsActivity.predictedData.setText((int) predicted);
        return predicted;
    }
}

