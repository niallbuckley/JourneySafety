package com.gmail.buckleyniall100.myapplication;

import android.os.AsyncTask;
import android.provider.DocumentsContract;

import com.gmail.buckleyniall100.myapplication.MapsActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class fetchData extends AsyncTask<Void, Void, Void> {
    private final double lat;
    private final double lon;
    String data = "";
    String osmId = "";
    String roadData = "";
    String maxSpeed = "";

    public fetchData(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL myURL = new URL("https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat="+ lat+ "&lon="+ lon +"&zoom=17");
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
                osmId = (String) jsonData.get("osm_id");
                getSpeedLimit(osmId);
                //getMaxSpeed max = new getMaxSpeed(osmId);

            } catch (JSONException e) {
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
        super.onPostExecute(aVoid);

        MapsActivity.data.setText(this.maxSpeed);
    }

    private String getSpeedLimit(String ID){
        try {
            URL roadURL = new URL("https://www.openstreetmap.org/api/0.6/way/" + ID);
            HttpURLConnection httpURLConnection = (HttpURLConnection) roadURL.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null){
                line = bufferedReader.readLine();
                roadData = roadData + line;
            }
            System.out.println("road data: " + roadData );
            String xml = roadData;
            //String xml = " <way id=\"22635043\" visible=\"true\" version=\"17\" changeset=\"56463008\" timestamp=\"2018-02-18T13:18:01Z\" user=\"drnoble\" uid=\"388774\">  <nd ref=\"472727904\"/>  <nd ref=\"5082694905\"/>  <nd ref=\"354517\"/>  <nd ref=\"5420216632\"/>  <nd ref=\"444521\"/>  <nd ref=\"354306\"/>  <tag k=\"busway:right\" v=\"lane\"/>  <tag k=\"cycleway:right\" v=\"share_busway\"/>  <tag k=\"highway\" v=\"trunk\"/>  <tag k=\"maxspeed\" v=\"50\"/>  <tag k=\"name\" v=\"Western Road\"/>  <tag k=\"ref\" v=\"N22\"/> </way>";
            //String xml = " <?xml version=\"1.0\" encoding=\"UTF-8\"?><osm version=\"0.6\" generator=\"CGImap 0.6.1 (1910 thorn-01.openstreetmap.org)\" copyright=\"OpenStreetMap and contributors\" attribution=\"http://www.openstreetmap.org/copyright\" license=\"http://opendatacommons.org/licenses/odbl/1-0/\"> <way id=\"22635043\" visible=\"true\" version=\"17\" changeset=\"56463008\" timestamp=\"2018-02-18T13:18:01Z\" user=\"drnoble\" uid=\"388774\">  <nd ref=\"472727904\"/>  <nd ref=\"5082694905\"/>  <nd ref=\"354517\"/>  <nd ref=\"5420216632\"/>  <nd ref=\"444521\"/>  <nd ref=\"354306\"/>  <tag k=\"busway:right\" v=\"lane\"/>  <tag k=\"cycleway:right\" v=\"share_busway\"/>  <tag k=\"highway\" v=\"trunk\"/>  <tag k=\"maxspeed\" v=\"50\"/>  <tag k=\"name\" v=\"Western Road\"/>  <tag k=\"ref\" v=\"N22\"/> </way></osm>null";
            String s1 = xml.substring(xml.indexOf("maxspeed")+1);
            s1.trim();
            Pattern p = Pattern.compile("\"[0-9]+\"");
            Matcher m = p.matcher(s1);
            System.out.println("s1 String 1 is equal to: " + s1);
            if (m.find()) {
                System.out.println(m.group());
                maxSpeed = m.group();
                maxSpeed = maxSpeed.replaceAll("^\"|\"$", "");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }*/

        return null;
    }
}
