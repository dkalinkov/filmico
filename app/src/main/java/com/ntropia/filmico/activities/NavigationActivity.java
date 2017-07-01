package com.ntropia.filmico.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.ntropia.filmico.R;
import com.ntropia.filmico.fragments.LandingScreen;
import com.ntropia.filmico.models.Movie;
import com.ntropia.filmico.utilities.ApiRequester;
import com.ntropia.filmico.utilities.Mapper;
import com.ntropia.filmico.utilities.UrlBulder;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LandingScreen landingScreenFragment;
    private ProgressBar activityProgressBar;
    private final int animationDelay = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        activityProgressBar = (ProgressBar) findViewById(R.id.navigationActivityProgressBar);
        activityProgressBar.setAlpha(0f);
        activityProgressBar.setVisibility(View.INVISIBLE);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.movies_popular) {
            this.loadPopularMoviesFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadPopularMoviesFragment() {
        LandingScreen lsf = LandingScreen.newInstance();
        startProgressBarAnimation();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.landingScreenLayout, lsf);
        ft.commit();

        landingScreenFragment = lsf;

        String url = UrlBulder.generateUrlAddress(getString(R.string.api_url),
                getString(R.string.api_key),
                getString(R.string.api_movies_popular),
                null);

        new RetrieveMoviesTask().execute(url);
    }

    private void startProgressBarAnimation() {
        activityProgressBar.setAlpha(0f);
        activityProgressBar.setVisibility(View.VISIBLE);
        activityProgressBar.animate().alpha(1f).setDuration(animationDelay);
    }

    private void stopProgressBarAnimation() {
        activityProgressBar.setAlpha(1f);
        activityProgressBar.animate().alpha(0f).setDuration(animationDelay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                activityProgressBar.setVisibility(View.GONE);
            }
        });
    }

    class RetrieveMoviesTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            return new ApiRequester().get(params[0]);
        }

        protected void onPostExecute(String jsonMovies) {
            try {
                Movie[] movies = new Mapper().mapToMovie(jsonMovies);

                if (landingScreenFragment != null) {
                    for (Movie m: movies) {
                        landingScreenFragment.addMovie(m);
                    }

                    stopProgressBarAnimation();

                    landingScreenFragment.getView().setAlpha(0f);
                    landingScreenFragment.getView().animate().alpha(1f).setDuration(animationDelay);
                }
            } catch (Exception ex) {
                Log.d("Error", ex.getMessage(), ex);
            }
        }
    }
}
