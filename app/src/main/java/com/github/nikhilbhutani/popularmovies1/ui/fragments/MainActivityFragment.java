package com.github.nikhilbhutani.popularmovies1.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.nikhilbhutani.popularmovies1.R;
import com.github.nikhilbhutani.popularmovies1.adapters.RecyclerViewAdapter;
import com.github.nikhilbhutani.popularmovies1.model.Movie;
import com.github.nikhilbhutani.popularmovies1.network.FetchMovies;
import com.github.nikhilbhutani.popularmovies1.network.ListenerInterface;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Bhutani on 8/3/2016.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Movie> movies;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        movies = new ArrayList<>();


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }

        recyclerView.setLayoutManager(mLayoutManager);

        FetchMovies fetchMovies = new FetchMovies(getActivity(),listenerInterface);
        fetchMovies.execute();

        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movies);
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }


    ListenerInterface listenerInterface = new ListenerInterface() {
        @Override
        public void response(List<Movie> movieList) {

            movies.clear();
            for (Movie movie : movieList) {
                movies.add(movie);
            }

            recyclerViewAdapter.notifyDataSetChanged();



        }
    };
}
