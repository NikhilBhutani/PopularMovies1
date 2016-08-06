package com.github.nikhilbhutani.popularmovies1.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.nikhilbhutani.popularmovies1.R;
import com.github.nikhilbhutani.popularmovies1.adapters.RecyclerViewAdapter;
import com.github.nikhilbhutani.popularmovies1.model.Movie;
import com.github.nikhilbhutani.popularmovies1.network.FetchMovies;
import com.github.nikhilbhutani.popularmovies1.network.ListenerInterface;
import com.github.nikhilbhutani.popularmovies1.ui.activities.MainActivity;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Nikhil Bhutani on 8/3/2016.
 */
public class MainActivityFragment extends Fragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Movie> movies;
    public View view;
    FetchMovies fetchMovies;


    public static String DETAILS = "details";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_topRated) {


            String TopRated = "top_rated";
            fetchMovies = new FetchMovies(getActivity(), view, listenerInterface, TopRated);
            fetchMovies.execute();
            fetchMovies.progressDialog.show();
            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movies);
            recyclerView.setAdapter(recyclerViewAdapter);

            MainActivity.actionBar.setTitle(R.string.topRated_movies_title_actionBar);
            return true;
        }

        if (id == R.id.action_popular) {


            String TopRated = "popular";
            fetchMovies = new FetchMovies(getActivity(), view, listenerInterface, TopRated);
            fetchMovies.execute();
            fetchMovies.progressDialog.show();
            recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), movies);
            recyclerView.setAdapter(recyclerViewAdapter);
            MainActivity.actionBar.setTitle(R.string.popular_movies_title_actionBar);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
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

        if (savedInstanceState == null || !savedInstanceState.containsKey("Movies")) {
            fetchMovies = new FetchMovies(getActivity(), view, listenerInterface);
            fetchMovies.execute();
            fetchMovies.progressDialog.show();

        } else {
            movies = savedInstanceState.getParcelableArrayList("Movies");
        }

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

    @Override
    public void onPause() {
        super.onPause();
        if (FetchMovies.progressDialog != null && FetchMovies.progressDialog.isShowing()) {

            FetchMovies.progressDialog.dismiss();
            FetchMovies.progressDialog = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("OnSaveInstanceState");
        outState.putParcelableArrayList("Movies", (ArrayList<? extends Parcelable>) movies);

    }

}
