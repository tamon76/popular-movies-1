package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String mBaseImagePath = getString(R.string.base_image_url);
        String mImageSize = getString(R.string.image_size);

        TextView mTitle = findViewById(R.id.movieTitle_tv);
        ImageView mMovieImage = findViewById(R.id.movieImage_iv);
        TextView mOverview = findViewById(R.id.overview_tv);
        TextView mRating = findViewById(R.id.rating_tv);
        TextView mDate = findViewById(R.id.releaseDate_tv);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");

        mTitle.setText(movie.getOriginalTitle());
        mOverview.setText(movie.getOverview());
        mRating.setText(movie.getVoteAverage());
        mDate.setText(movie.getReleaseDate());

        String mPosterPath = movie.getThumbnailPath();

        if (mPosterPath.equals("null")) {
            mMovieImage.setImageResource(R.drawable.film_reel);
        } else {
            mPosterPath = mBaseImagePath + mImageSize + mPosterPath;
            Picasso.with(this).load(mPosterPath).into(mMovieImage);
        }
    }
}
