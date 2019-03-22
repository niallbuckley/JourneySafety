package com.gmail.buckleyniall100.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Graph extends Fragment {
    private String userId = "userTest";
    private Random rand = new Random();
    int n = rand.nextInt(50) + 1;
    Date oldDate = new Date("Mon Jan 28 21:09:54 GMT 2019");
    Date maybe = new Date("Mon Jan 28 22:29:16 GMT 2019");
    int numErrors = 20;
    private static final String TAG = "MainActivity";
    private LineChart mChart;
    int count =1;
    public List<Long> list = new ArrayList<Long>();
    String strDataSet = "";
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_graph, container, false);
        FirebaseApp.initializeApp(getActivity());
        String[] eArray = getActivity().getResources().getStringArray(R.array.names);
        //basicReadWrite();
        //Drawgraph();
        Spinner mySpinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, eArray);
        System.out.println(myAdapter);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        final String first = mySpinner.getSelectedItem().toString().toString();
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if(selected == first){
                    System.out.println("Yuppp");
                    strDataSet = "Last 7 trips";
                    basicReadWrite(7);
                    /*ILineDataSet dataSet = set1;
                    LineData data = new LineData(dataSet);
                    mChart.setData(data);*/
                }
                else{
                    System.out.println("Here");
                    strDataSet = "Last 30 trips";
                    basicReadWrite(30);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }


    public void onViewCreated(View v, Bundle savedInstanceState){


    }


    public void basicReadWrite(int period) {
        // [START write_message]
        // Write a message to the database
        Date date = new Date();
        DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("users");
        //Query dataOrdered = myRef.orderByKey();
        Query recentPostsQuery = db.child(userId).orderByKey().limitToLast(period);
        //int score = calculateScore(numErrors, oldDate, maybe);
        //User user = new User(score, date);
        final ArrayList<Entry> yValue1 = new ArrayList<>();
        recentPostsQuery.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            System.out.println("Ordered?? " + child.getValue());
                            JSONObject val = new JSONObject((Map) child.getValue());
                            Long score = null;
                            try {
                                score = (Long) val.get("score");
                                System.out.println(score);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            yValue1.add(new Entry(count++, (long) score));
                            //add to graph list
                            //testing

                        }
                        Drawgraph((ArrayList) yValue1);
                        count = 1;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                }
        );
    }
    public void Drawgraph(ArrayList<Entry> list) {
        mChart = (LineChart) v.findViewById(R.id.line_Chart);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        //mChart.setBackgroundColor(Color.BLUE);
        /*ArrayList<Entry> yValue2 = new ArrayList<>();

        ArrayList<Entry> yValue1 = new ArrayList<>();
        for (int j = 0; j < list.size(); j++){
            yValue1.add(new Entry(j, (Long) list.get(j)));
            //System.out.println("Hello "+ list.get(j));
        }
        yValue2.add(new Entry(0, 1));
        yValue2.add(new Entry(1, 5));
        yValue2.add(new Entry(2, 7));
        yValue2.add(new Entry(3, 3));
        yValue2.add(new Entry(4, 6));
        yValue2.add(new Entry(5, 5));*/
        final LineDataSet set1 = new LineDataSet(list, strDataSet);
        //final LineDataSet set2 = new LineDataSet(yValue2, "Data Set 2");

        set1.setFillAlpha(110);

        //ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        //dataSets.add(set1);
        //ILineDataSet dataSet = set1;
        //applyData(set1, set2);
        //LineData data = new LineData(dataSet);
        ILineDataSet dataSet = set1;
        LineData data = new LineData(dataSet);
        mChart.setData(data);

        System.out.println("U make it");
        mChart.invalidate();

        //mChart.setData(data);*/
        //set1.setVisible();
    }

}

