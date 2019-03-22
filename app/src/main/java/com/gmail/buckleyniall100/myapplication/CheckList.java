package com.gmail.buckleyniall100.myapplication;

import android.app.assist.AssistStructure;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckList extends AppCompatActivity implements View.OnClickListener {

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;
    private CheckBox checkBox7;
    private CheckBox checkBox8;
    String data = "";
    EditText hoursSlept;
    EditText hoursSinceSlept;
    private int ticked;
    private int intHoursSinceSlept;
    private int intHoursSlept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        Button continueButton = (Button) findViewById(R.id.the_button);
        continueButton.setOnClickListener(CheckList.this);

        FetchWeather weather = new FetchWeather();
        weather.execute();
        checkBox1 = (CheckBox) findViewById(R.id.check_box1);
        checkBox2 = (CheckBox) findViewById(R.id.check_box2);
        checkBox3 = (CheckBox) findViewById(R.id.check_box3);
        checkBox4 = (CheckBox) findViewById(R.id.check_box4);
        checkBox5 = (CheckBox) findViewById(R.id.check_box5);
        checkBox6 = (CheckBox) findViewById(R.id.check_box6);
        checkBox7 = (CheckBox) findViewById(R.id.check_box7);
        checkBox8 = (CheckBox) findViewById(R.id.check_box8);

        hoursSlept = (EditText) findViewById(R.id.input1);
        hoursSinceSlept = (EditText) findViewById(R.id.input2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.the_button:
                userSleepTime();
                System.out.println("Number Ticked: " + getNumberTicked());
                int ticked = getNumberTicked();
                Intent i = new Intent(CheckList.this, MapsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("numTicked",ticked);
                extras.putInt("hoursSlept", intHoursSlept);
                extras.putInt("hoursSinceSlept", intHoursSinceSlept);
                i.putExtras(extras);
                startActivity(i);
                //startActivity(new Intent(this, SleepTime.class));
                break;
        }
    }

    private void userSleepTime() {
        intHoursSlept = Integer.parseInt(hoursSlept.getText().toString());
        intHoursSinceSlept = Integer.parseInt(hoursSinceSlept.getText().toString());
        if(intHoursSlept > 24){

        }
        if(intHoursSinceSlept > 24){

        }
    }

    private int getNumberTicked() {
        int count = 0;
        if (checkBox1.isChecked()) {
            count++;
        }
        if (checkBox2.isChecked()) {
            count++;
        }
        if (checkBox3.isChecked()) {
            count++;
        }
        if (checkBox4.isChecked()) {
            count++;
        }
        if (checkBox5.isChecked()) {
            count++;
        }
        if (checkBox6.isChecked()) {
            count++;
        }
        if (checkBox7.isChecked()) {
            count++;
        }
        if (checkBox8.isChecked()) {
            count++;
        }
        return count;
    }
}
