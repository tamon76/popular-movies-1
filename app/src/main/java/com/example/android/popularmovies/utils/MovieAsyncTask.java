package com.example.android.popularmovies.utils;

import android.os.AsyncTask;
import com.example.android.popularmovies.model.Movie;

public class MovieAsyncTask extends AsyncTask<String, Void, Movie[]> {

    private final OnTaskCompleted taskCompleted;
    private final String sortBy;

    public MovieAsyncTask(OnTaskCompleted activityContext, String sortBy) {
        this.taskCompleted = activityContext;
        this.sortBy = sortBy;
    }

    @Override
    protected Movie[] doInBackground(String... urls) {
        if ((urls.length < 1) || urls[0] == null) {
            return null;
        }
        return JsonUtils.fetchMovies(sortBy);
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        taskCompleted.onTaskCompleted(movies);
    }
}
