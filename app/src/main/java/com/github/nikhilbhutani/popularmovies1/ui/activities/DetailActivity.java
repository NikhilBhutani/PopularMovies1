package com.github.nikhilbhutani.popularmovies1.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.nikhilbhutani.popularmovies1.R;
import com.github.nikhilbhutani.popularmovies1.model.Movie;
import com.github.nikhilbhutani.popularmovies1.ui.fragments.MainActivityFragment;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Nikhil Bhutani on 8/3/2016.
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.language)
    TextView movieLanguage;

    @BindView(R.id.releaseDate)
    TextView movieReleaseDate;

    @BindView(R.id.movieSummary)
    TextView movieSummary;

    @BindView(R.id.movieDetailTitle)
    TextView movieDetialedTitle;

    @BindView(R.id.posterImageDetail)
    ImageView moviePosterImage;

    @BindView(R.id.voteAverage)
    TextView voteAverage;
    Movie movie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        if (savedInstanceState == null || !savedInstanceState.containsKey("SavedMovie")) {
        } else {
            savedInstanceState.getParcelable("SavedMovie");
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.MovieDetail_title_actionBar);
        }

        /*
        movieLanguage = (TextView) findViewById(R.id.language);
        movieReleaseDate = (TextView) findViewById(R.id.releaseDate);
        movieSummary = (TextView) findViewById(R.id.movieSummary);
        movieDetialedTitle = (TextView) findViewById(R.id.movieDetailTitle);
        moviePosterImage = (ImageView) findViewById(R.id.posterImageDetail);
        voteAverage = (TextView) findViewById(R.id.voteAverage);
         */

        Intent intent = this.getIntent();


        if (intent != null && intent.hasExtra(MainActivityFragment.DETAILS)) {
            movie = intent.getParcelableExtra(MainActivityFragment.DETAILS);
            displayDetails(movie);
        }

    }

    private void displayDetails(Movie movie) {

        movieLanguage.setText(movie.getOriginalLanguage());
        movieDetialedTitle.setText(movie.getOriginalTitle());
        movieReleaseDate.setText(movie.getReleaseDate());
        movieSummary.setText(movie.getOverview());
        voteAverage.setText(String.valueOf(movie.getVoteAverage()));
        Glide.with(this).load(movie.getPosterPath()).into(moviePosterImage);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("SavedMovie", movie);
    }
}
