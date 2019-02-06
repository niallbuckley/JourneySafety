package com.gmail.buckleyniall100.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    Button startJourneyButton, journeyHisButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button startJourneyButton = (Button) findViewById(R.id.first_button);
        startJourneyButton.setOnClickListener(MainMenu.this);
        journeyHisButton = (Button) findViewById(R.id.second_button);
        journeyHisButton.setOnClickListener(MainMenu.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.first_button:
                startActivity(new Intent(this, CheckList.class));
                break;
            case R.id.second_button:
                //startActivity(new Intent(this, SignUp.class));
                break;
        }

    }
}
