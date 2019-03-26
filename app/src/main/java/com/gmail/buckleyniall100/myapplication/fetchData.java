package com.gmail.buckleyniall100.myapplication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fetchData extends AsyncTask<Void, Void, Void> {
    private static String address;
    private final double lat;
    private final double lon;
    String data = "";
    int osmId ;
    //String address = "";
    String roadData = "";
    String maxSpeed = "";
    String highway = "";
    boolean junction = false;

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
                System.out.println(data);
                JSONObject jsonData = new JSONObject(data);
                osmId = (int) jsonData.get("osm_id");
                System.out.println("Place ID" + osmId);
                address = (String) jsonData.get("display_name");
                System.out.println("ADDRESS: "+ address);
                setAddress(address);
                getSpeedLimit(osmId);
                //getMaxSpeed max = new getMaxSpeed(osmId);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch ( java.lang.RuntimeException e) {
                System.out.println("Failed");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void setAddress(String address) {
        this.address = address;
    }
    public static String getAddress(){
        return address;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MapsActivity.data.setText(this.maxSpeed);
        isJunction.setJunction(this.junction);
    }

    private String getSpeedLimit(int ID){
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
            String s2 = xml.substring(xml.indexOf("ref")+1);
            String s1 = xml.substring(xml.indexOf("maxspeed")+1);
            System.out.println(xml);
            //s1.trim();
            s2.trim();
            s1.trim();
            Pattern p = Pattern.compile("\"[0-9]+\"");
            //Matcher m = p.matcher(s1);
            Matcher m1 = p.matcher(s2);
            System.out.println("s1 String 1 is equal to: " + s2);
            if (m1.find()) {
                System.out.println(m1.group());
                String ref = m1.group();
                ref = ref.replaceAll("^\"|\"$", "");
                System.out.println("HELLO " + ref);
                containsJunction(ref);

            }
            Matcher m2 = p.matcher(s1);
            System.out.println("s1 String 1 is equal to: " + s1);
            if (m2.find()) {
                System.out.println(m2.group());
                maxSpeed = m2.group();
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
    private void containsJunction(String ref) {
        try {
            URL roadURL = new URL("https://www.openstreetmap.org/api/0.6/node/" + ref);
            HttpURLConnection httpURLConnection = (HttpURLConnection) roadURL.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while (line != null) {
                line = bufferedReader.readLine();
                highway = highway + line;
            }
            System.out.println("highway data: " + highway);
            String xml = highway;
            if(xml.contains("highway")){
                junction = true;
                System.out.println("ISIT???");
            }
            else{
                junction = false;
            }
            System.out.println("F/T?" + junction);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
