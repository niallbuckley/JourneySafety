package com.gmail.buckleyniall100.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class StartJourney extends Fragment implements View.OnClickListener {
    Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start_journey, container,false);

        btn = (Button) rootView.findViewById(R.id.the_button);
        btn.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.the_button:
                System.out.println("HAAYYYYYYY");
                Intent intent = new Intent(getActivity(), CheckList.class);
                startActivity(intent);
                //((Activity) getActivity()).overridePendingTransition(0,0);
        }
    }

    public static String setWeather(String weather){
        System.out.println("The current weather  == " + weather);
        return weather;
    }
}
