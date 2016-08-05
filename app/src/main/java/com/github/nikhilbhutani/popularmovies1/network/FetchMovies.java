package com.github.nikhilbhutani.popularmovies1.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
    public ProgressDialog progressDialog;
    Context mcontext;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    public FetchMovies(Context context, ListenerInterface myInterface) {

        if (myInterface == null) {
            throw new NullPointerException("Listener cant be null");
        }

        this.mcontext = context;
        this.setListener(myInterface);

        this.progressDialog = new ProgressDialog(context);
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

        JSONObject jsonObject = new JSONObject(completeJSONString);

        JSONArray jsonArray = jsonObject.getJSONArray("results");

        for (int i = 0; i < 20; i++) {

            JSONObject finaljsonObject = jsonArray.getJSONObject(i);


            Movie movie = new Gson().fromJson(finaljsonObject.toString(), Movie.class);

            //     System.out.println(movie.getTitle());

            movies.add(movie);

        }

        return movies;
    }


    @Override
    protected List<Movie> doInBackground(Void... voids) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String completeJSONString = null;


        final String api_key = "ENTER API KEY HERE";
        final String API_KEY = "api_key";
        //  final String QUERY_POPULAR = "popular"

        Uri builderUri = Uri.parse(Constants.API_URL).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();

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

        progressDialog.dismiss();
        if (delegate != null) {
            delegate.response(myMovieList);
        }
    }
}
