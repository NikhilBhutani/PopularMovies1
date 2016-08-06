package com.github.nikhilbhutani.popularmovies1.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.github.nikhilbhutani.popularmovies1.R;
import com.github.nikhilbhutani.popularmovies1.model.Movie;

import com.github.nikhilbhutani.popularmovies1.ui.fragments.MainActivityFragment;
import com.github.nikhilbhutani.popularmovies1.utils.Constants;
import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikhil Bhutani on 8/4/2016.
 */
public class FetchMovies extends AsyncTask<Void, Void, List<Movie>> {


    private final String LOG_TAG = FetchMovies.class.getSimpleName();
    ListenerInterface delegate;
    Context mcontext;
    JSONObject jsonObject;
    JSONArray jsonArray;
    public static ProgressDialog progressDialog;
    String sortParam = "popular";
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    View view;
    Uri builderUri;

    public FetchMovies(Context context, View view1, ListenerInterface myInterface) {

        if (myInterface == null) {
            throw new NullPointerException("Listener cant be null");
        }

        this.mcontext = context;
        this.setListener(myInterface);

        progressDialog = new ProgressDialog(context);
        this.view = view1;
    }


    public FetchMovies(Context context, View view1, ListenerInterface myInterface, String topRated) {

        if (myInterface == null) {
            throw new NullPointerException("Listener cant be null");
        }

        this.mcontext = context;
        this.setListener(myInterface);

        this.progressDialog = new ProgressDialog(context);
        this.view = view1;
        this.sortParam = topRated;
    }


    public void setListener(ListenerInterface listener) {
        this.delegate = listener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Please wait...");
    }


    private List<Movie> getMovies(String completeJSONString) throws JSONException {

        List<Movie> movies = new ArrayList<>();
        if (completeJSONString != null) {
            jsonObject = new JSONObject(completeJSONString);

            jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < 20; i++) {

                JSONObject finaljsonObject = jsonArray.getJSONObject(i);


                Movie movie = new Gson().fromJson(finaljsonObject.toString(), Movie.class);

                //     System.out.println(movie.getTitle());

                movies.add(movie);

            }
        }
        return movies;
    }


    @Override
    protected List<Movie> doInBackground(Void... voids) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String completeJSONString = null;


        final String api_key = "ENTER YOUR API KEY HERE";
        final String API_KEY = "api_key";
        //  final String QUERY_POPULAR = "popular"

        if (sortParam == "top_rated") {

            builderUri = Uri.parse(Constants.TOP_RATED_MOVIES_API_URL).buildUpon()
                    .appendQueryParameter(API_KEY, api_key)
                    .build();

        } else if (sortParam == "popular") {

            builderUri = Uri.parse(Constants.POPULAR_MOVIES__API_URL).buildUpon()
                    .appendQueryParameter(API_KEY, api_key)
                    .build();

        }

        Log.v(LOG_TAG, builderUri.toString());

        try {
            URL url = new URL(builderUri.toString());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line + "\n");

            }


            completeJSONString = stringBuffer.toString();

            Log.v(LOG_TAG, completeJSONString);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            Snackbar.make(view, R.string.no_network, Snackbar.LENGTH_LONG).show();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            return getMovies(completeJSONString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> myMovieList) {

        connectivityManager = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {

            progressDialog.dismiss();
            Snackbar.make(view, R.string.no_network, Snackbar.LENGTH_LONG).show();
        } else {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            if (delegate != null) {
                delegate.response(myMovieList);
            }
        }
    }


}
