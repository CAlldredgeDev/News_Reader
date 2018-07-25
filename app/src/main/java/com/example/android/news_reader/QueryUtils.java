package com.example.android.news_reader;

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
import java.util.List;

/**
 * Helper methods related to requesting and receiving response data from The Guardian.
 */
public final class QueryUtils {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Article> extractArticleFromJson(String articleJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Empty ArrayList that we can start adding earthquakes to
        List<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            // Now that we have the base response lets find the "response" in the data to being
            // gathering the results.
            JSONObject response = baseJsonResponse.getJSONObject("response");

            // Now that we have the response lets grab the "results" key and begin to gather
            // the relavent properties.
            JSONArray articleArray = response.getJSONArray("results");

            // Pre figure the length of the array so that it is only called once instead of
            // every iteration of the loop.
            int totalArticles = articleArray.length();

            // For each earthquake in the earthquakeArray, create an {@link Article} object
            for (int i = 0; i < totalArticles; i++) {

                // Get a single Article at position i within the list of earthquakes
                JSONObject currentArticle = articleArray.getJSONObject(i);

                // Extract the value for the key called "webPublicationDate"
                String webPublicationDate = currentArticle.getString("webPublicationDate");

                // Extract the value for the key called "webTitle"
                String webTitle = currentArticle.getString("webTitle");

                // Extract the value for the key called "sectionName"
                String sectionName = currentArticle.getString("sectionName");

                // Extract the value for the key called "webUrl"
                String webUrl = currentArticle.getString("webUrl");

                // Create a new {@link Article} object with the webTitle, sectionName,
                // webPublicationDate and webUrl from the JSON response.
                Article article = new Article(webTitle, sectionName, webPublicationDate, webUrl);

                // Add the new {@link article} to the list of articles.
                articles.add(article);
            }

        } catch (JSONException e) {
            // If an error is thrown in the above "try" block, catch the exception here, so the app
            // doesn't crash. Print a log message with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of articles
        return articles;
    }


    // Query The Guardian repository and return a list of {@link Article} objects.
    public static List<Article> fetchArticleData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Show the loading indicator for 2 sec. or 2000 millis.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Article}s
        // Return the list of {@link Article}s
        return extractArticleFromJson(jsonResponse);
    }


    // Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was good (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    // Convert the {@link InputStream} into a String which contains the
    // whole JSON response from the server.
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}