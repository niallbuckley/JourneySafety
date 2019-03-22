package com.gmail.buckleyniall100.myapplication;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FetchWeather extends AsyncTask<Void, Void, Void> {
    String data = "";
    String weather = "";
    @Override
    protected Void doInBackground(Void... voids) {
        try{
            URL myURL = new URL("http://api.openweathermap.org/data/2.5/weather?q=galway,Ireland&APPID=26431758f2fd746870620e61b6310840");
            HttpURLConnection httpURLConnection = (HttpURLConnection) myURL.openConnection();
            httpURLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            //int status = httpURLConnection.getResponseCode();
            InputStream inputStream = httpURLConnection.getInputStream();
            //System.out.println("Stat " + status);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray x = jsonData.getJSONArray("weather");
                JSONObject y = x.getJSONObject(0);
                weather = y.getString("main");
            }catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        StartJourney.setWeather(weather);
    }

}
