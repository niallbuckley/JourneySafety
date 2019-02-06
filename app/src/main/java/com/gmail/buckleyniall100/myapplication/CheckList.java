package com.gmail.buckleyniall100.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckList extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        Button startJourneyButton = (Button) findViewById(R.id.first_button);
        startJourneyButton.setOnClickListener(CheckList.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.the_button:
                startActivity(new Intent(this, SleepTime.class));
                break;
    }
}
}
