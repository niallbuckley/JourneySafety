package com.gmail.buckleyniall100.myapplication;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.stream.IntStream;

public class predictScore {
    static double[][][] TRAINING_DATA;
    static LinearRegression lr;
    String userId = "userTest";
    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users");
    private int count=0;
    private boolean first = true;
    //private double[][][] TRAINING_DATA2;
    Double[][][] TRAINING_DATA2 = new Double[10][2][3];
    //static Double[][][] TRAINING_DATA2;


    @SuppressLint("NewApi")
    public void predictScore(int ticked, int hoursSlept, int hoursSinceSlept) {
        System.out.println("HERE");
        setUpData();
        /*System.out.println("NULL?? " + Testing[0][0][0]);
        double[][] xArray = new double[TRAINING_DATA.length][TRAINING_DATA[0][0].length];
        double[][] yArray = new double[TRAINING_DATA.length][1];
        //need to understand this line better.
        IntStream.range(0, TRAINING_DATA.length).forEach(i -> {
            IntStream.range(0, TRAINING_DATA[0][0].length).forEach(j -> xArray[i][j] = TRAINING_DATA[i][0][j]);
            yArray[i][0] = TRAINING_DATA[i][1][0];
        });
        System.out.println("Yup " + xArray);
        System.out.println("Hi " +yArray);
        try {
            lr = new LinearRegression(xArray, yArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Estimated rent: " + lr.estimateRent());
        return lr.estimateRent();*/
    }

    private Double[][][] setUpData() {
        TRAINING_DATA = new double[][][]{{{1.0, 400, 2, 1000}, {800}},
                {{1.0, 450, 2, 1000}, {820}}, {{1.0, 500, 4, 900}, {980}}, {{1.0, 600, 3, 900}, {820}}, {{1.0, 650, 4, 100}, {930}}};
        //TRAINING_DATA2 = new double[][][];
        //return TRAINING_DATA;//Query dataOrdered =
        Query recentPostsQuery = db.child(userId).orderByKey().limitToLast(10);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("GOTHERE");
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    System.out.println("Ordered?? " + child.getValue());
                    JSONObject val = new JSONObject((Map) child.getValue());
                    try {
                        TRAINING_DATA2[count][0][0]= Double.valueOf((Long) val.get("hoursSlept"));
                        TRAINING_DATA2[count][0][1]= Double.valueOf((Long) val.get("hoursSinceSlept"));
                        TRAINING_DATA2[count][0][2]= Double.valueOf((Long) val.get("ticked"));
                        TRAINING_DATA2[count][1][0]= Double.valueOf((Long) val.get("score"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("TD: " + TRAINING_DATA2[0][0]);
                    System.out.println("TD: " + TRAINING_DATA2[0]);
                    System.out.println("TD: " + TRAINING_DATA2[count][0][0]);
                    count ++;
                }
                predictScore1(TRAINING_DATA2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    System.out.println("IS IT NULL?? " + TRAINING_DATA2[0][0][0]);
    return TRAINING_DATA2;
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
            System.out.println("Index i+ " + i);
            for(int j= 0; j<3; j++){
                System.out.println("Index j+ " + j);
                xArray[i][j] = Testing[i][0][j];
                yArray[i][0] = Testing[i][1][0];
            }
        }
        System.out.println("chechh");
        for (int i = 0; i < xArray.length; i++) {
            System.out.println("Hi?");
            for (int j = 0; j < xArray[i].length; j++) {
                System.out.println("HI");
                System.out.print(xArray[i][j] + " XARRAY");
            }
        }
        try {
            lr = new LinearRegression(xArray, yArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Estimated rent: " + lr.estimateRent());
        return lr.estimateRent();
    }


}

