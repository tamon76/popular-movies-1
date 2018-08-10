package com.example.android.popularmovies.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import  com.example.android.popularmovies.model.Movie;
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

public class JsonUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static Movie[] fetchMovies(String sort) {

        URL url = getSortedUrl(sort);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return extractFeatureFromJson(jsonResponse);
    }

    private static URL getSortedUrl(String sortOrder) {
        try {
            Uri builtUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendPath(sortOrder)
                    .appendQueryParameter("api_key", API_KEY)
                    .build();

            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
            return null;
        }
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to server");
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

    private static Movie[] extractFeatureFromJson(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        final String RESULTS = "results";
        final String ORIGINAL_TITLE = "original_title";
        final String THUMBNAIL = "poster_path";
        final String OVERVIEW = "overview";
        final String RATING = "vote_average";
        final String RELEASE_DATE = "release_date";

        JSONObject movieJson;
        String title;
        String thumbnail;
        String overview;
        String rating;
        String releaseDate;

        try {
            movieJson = new JSONObject(json);
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);

            Movie[] movieList = new Movie[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {

                movieList[i] = new Movie();
                JSONObject currentMovie = movieArray.getJSONObject(i);

                title = currentMovie.getString(ORIGINAL_TITLE);
                thumbnail = currentMovie.getString(THUMBNAIL);
                overview = currentMovie.getString(OVERVIEW);
                rating = currentMovie.getString(RATING);
                releaseDate = currentMovie.getString(RELEASE_DATE);

                movieList[i].setOriginalTitle(title);
                movieList[i].setThumbnailPath(thumbnail);
                movieList[i].setOverview(overview);
                movieList[i].setVoteAverage(rating);
                movieList[i].setReleaseDate(releaseDate);
            }
            return movieList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing JSON", e);
            return null;
        }
    }
}
