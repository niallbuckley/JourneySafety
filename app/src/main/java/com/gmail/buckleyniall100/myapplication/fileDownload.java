package com.gmail.buckleyniall100.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class fileDownload extends AppCompatActivity implements View.OnClickListener {

    private JSONObject jsonObj;
    private String FILE_NAME;
    private TextView fileText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_download);

        TextView scoreText = findViewById(R.id.text1);
        TextView predictText = findViewById(R.id.text2);
        String predictedScore = getIntent().getStringExtra("predictedScore");
        String journeyScore = getIntent().getStringExtra("journeyScore");

        scoreText.append(journeyScore);
        predictText.append(predictedScore);

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener((View.OnClickListener) fileDownload.this);
        try {
            jsonObj = new JSONObject(getIntent().getStringExtra("journeyDetails"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObj);
        try {
            final String FILE_NAME = "Mistakes: " + jsonObj.get("Date/ Time") + ".txt";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:
                try {
                    save();
                    load();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }


    public void save() throws JSONException {
        final String FILE_NAME = "Mistakes: " + jsonObj.get("Date/ Time") + ".txt";
        FileOutputStream fos = null;
        JSONArray array = jsonObj.getJSONArray("Error");
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            //On date d/m/y After x sleep and y hours between sleep you made l errors:
            for (int i = 1; i < jsonObj.getJSONArray("Error").length(); i++) {
                String strJson = Integer.toString(i+1) + ". " + array.getString(i) + "\n";

                System.out.println(array.getString(i));
                //String strJson = Integer.toString(i + 1);
                fos.write(strJson.getBytes());
            }

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void load() throws JSONException {
        FileInputStream fis = null;
        TextView fileText = findViewById(R.id.predicted_score);
        final String FILE_NAME = "Mistakes: " + jsonObj.get("Date/ Time") + ".txt";
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

                while ((text = br.readLine()) != null) {
                    System.out.println("SB "+ sb);
                    sb.append(text).append("\n");
            }
            System.out.println("SB2 " + sb);
            fileText.setText(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

