package com.example.callvideo.Service;

import android.text.TextUtils;
import android.util.Log;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.callvideo.Common.GlobalVars.LANGUAGE_CODES;

/*
        Class   :   Utility class defined to perform API requests

                    1.  Create URL object from String URL
                    2.  Make Http request to get JSON response
                    3.  Parse JSON and extract relevent data

 */
public class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    //  METHOD TO CREATE URL OBJECT FROM STRING ARGUMENT
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //  METHOD TO MAKE HTTP REQUEST AND FETCH JSON OUTPUT
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";//respone JSON return
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;//Create HTTP connection
        InputStream inputStream = null;//inputStream to received Input at Server with 8byte-froms
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();//recived data from server with 8bytes from
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                Log.e(LOG_TAG, url.toString());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //  UTILITY METHOD TO READ JSON RESPONSE FROM STREAM
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        //The StringBuilder in Java represents a mutable sequence of characters.
        // Since the String Class in Java creates an immutable sequence of characters,
        // the StringBuilder class provides an alternative to String Class, as it creates a mutable sequence of characters.
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();//read for each lines and return the output
            }
        }
        return output.toString();
    }

    //  METHOD TO EXTRACT TRANSLATION FROM JSON RESPONSE
    private static String extractFromJsonTranslation(String stringJSON) {
        String translation = "";
        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }
        // Try to parse the JSON response string.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(stringJSON);
            JSONArray stringArray = baseJsonResponse.getJSONArray("text");//get data form JSON when we put data before this in uriBuilder.append("test")
            translation = stringArray.getString(0);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }
        return translation;
    }

    //  METHOD TO EXTRACT LANGUAGE LIST FROM JSON RESPONSE
    private static ArrayList<String> extractFromJsonLanguages(String stringJSON) {
        ArrayList<String> languagesList = new ArrayList<>();
        if (TextUtils.isEmpty(stringJSON)) {
            return null;
        }
        // Try to parse the JSON response string.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(stringJSON);
            JSONObject baseJsonResponseLangs = baseJsonResponse.optJSONObject("langs");
            Iterator<String> iter = baseJsonResponseLangs.keys();
            LANGUAGE_CODES.clear();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    Object value = baseJsonResponseLangs.get(key);
                    languagesList.add(value.toString());
                    LANGUAGE_CODES.add(key);
                } catch (JSONException e) {
                    Log.e("QueryUtils", "Problem parsing the JSON results", e);
                }
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return languagesList;
    }

    //  PUBLIC METHOD TO FETCH TRANSLATION
    public static String fetchTranslation(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFromJsonTranslation(jsonResponse);
    }

    //  PUBLIC METHOD TO FETCH LANGUAGES
    public static ArrayList<String> fetchLanguages(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        return extractFromJsonLanguages(jsonResponse);
    }
}
