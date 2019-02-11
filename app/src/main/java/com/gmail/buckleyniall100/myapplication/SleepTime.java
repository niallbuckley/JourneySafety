package com.gmail.buckleyniall100.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SleepTime extends AppCompatActivity implements View.OnClickListener {
    EditText hoursSlept;
    EditText hoursSinceSlept;
    private int ticked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_time);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ticked = extras.getInt("key");
            System.out.println(ticked);
            //The key argument here must match that used in the other activity
        }

        Button continueButton = (Button) findViewById(R.id.the_button);
        continueButton.setOnClickListener(SleepTime.this);
        //journeyHisButton.setOnClickListener(MainMenu.this);

        hoursSlept = (EditText) findViewById(R.id.input1);
        hoursSinceSlept = (EditText) findViewById(R.id.input2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.the_button:
                int intHoursSlept = Integer.parseInt(hoursSlept.getText().toString());
                int intHoursSinceSlept = Integer.parseInt(hoursSinceSlept.getText().toString());
                Intent intent = new Intent(this, MapsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("numTicked",ticked);
                extras.putInt("hoursSlept", intHoursSlept);
                extras.putInt("hoursSinceSlept", intHoursSinceSlept);
                intent.putExtras(extras);
                startActivity(intent);
                //startActivity(new Intent(this, MapsActivity.class));
                break;
        }
    }
}
