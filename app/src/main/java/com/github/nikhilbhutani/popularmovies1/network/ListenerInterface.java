package com.github.nikhilbhutani.popularmovies1.network;

import com.github.nikhilbhutani.popularmovies1.model.Movie;

import java.util.List;

/**
 * Created by Nikhil Bhutani on 8/4/2016.
 */
public interface ListenerInterface {

    void response(List<Movie> movieList);
}

