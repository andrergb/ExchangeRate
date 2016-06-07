package com.android.argb.exchangerate;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParser {

    static String TAG = "JSONParser";

    public JSONParser() {
    }

    public JSONObject getJSONFromUrl(String url) {
        InputStream inputStream = null;
        JSONObject jObject = null;
        String json = "";

        // Making HTTP request
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            urlConnection.disconnect();
        } catch (IOException e) {
            Log.e(TAG, "HTTP request Error: " + e.toString());
        }

        try {
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append("n");

            inputStream.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "Buffer Error: " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "JSON Parser: " + e.toString());
        }

        // return JSON String
        return jObject;
    }
}