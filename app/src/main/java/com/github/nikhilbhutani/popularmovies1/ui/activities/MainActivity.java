package com.github.nikhilbhutani.popularmovies1.ui.activities;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.github.nikhilbhutani.popularmovies1.R;
import com.github.nikhilbhutani.popularmovies1.ui.fragments.MainActivityFragment;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static ActionBar actionBar;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
                    .commit();
        }

        if (actionBar != null) {
            actionBar.setTitle("Popular Movies");
        }
    }


    @Override
    public void onBackPressed() {

        if (exit) {
            super.onBackPressed();

        } else {
            Snackbar.make(this.findViewById(R.id.container), R.string.exit, Snackbar.LENGTH_SHORT).show();
            exit = true;
        }

    }
}
