package com.ntropia.filmico.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.ntropia.filmico.R;
import com.ntropia.filmico.fragments.EntityListFragment;
import com.ntropia.filmico.fragments.LikedListFragment;
import com.ntropia.filmico.models.Movie;
import com.ntropia.filmico.utilities.ApiRequester;
import com.ntropia.filmico.utilities.Mapper;
import com.ntropia.filmico.utilities.UrlBulder;

import java.util.Map;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EntityListFragment entityListFragmentFragment;
    private LikedListFragment likedListFragment;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.movies_now_playing) {
            this.loadMoviesFragment(R.string.api_movies_now_playing);
        } else if (id == R.id.movies_popular) {
            this.loadMoviesFragment(R.string.api_movies_popular);
        } else if (id == R.id.movies_top_rated) {
            this.loadMoviesFragment((R.string.api_movies_top_rated));
        } else if (id == R.id.liked_list) {
            this.loadLikedFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadMoviesFragment(int actionId) {
        EntityListFragment lsf = EntityListFragment.newInstance();
        startProgressBarAnimation();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.landingScreenLayout, lsf);
        ft.commit();

        entityListFragmentFragment = lsf;

        String url = UrlBulder.generateUrlAddress(getString(R.string.api_url),
                getString(R.string.api_key),
                getString(actionId),
                null);

        new RetrieveEntitiesListTask().execute(url);
    }

    private void loadLikedFragment() {
        LikedListFragment llf = LikedListFragment.newInstance();
        startProgressBarAnimation();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.landingScreenLayout, llf);
        ft.commit();

        likedListFragment = llf;

        String spKey = getString(R.string.app_shared_pref_key);
        final SharedPreferences sp = getBaseContext().getSharedPreferences(spKey, getBaseContext().MODE_PRIVATE);
        Map<String, ?> spKeys = sp.getAll();

        for (Map.Entry<String, ?> entry : spKeys.entrySet()) {
            if (!entry.getKey().isEmpty()) {
                String url = UrlBulder.generateUrlAddress(getString(R.string.api_url),
                        getString(R.string.api_key),
                        getString(R.string.api_movie),
                        entry.getKey(), null, null);

                new RetrieveEntityTask().execute(url);
            }
        }
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

    private class RetrieveEntitiesListTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            return new ApiRequester().get(params[0]);
        }

        protected void onPostExecute(String jsonMovies) {
            try {
                Movie[] movies = new Mapper().mapToMovie(jsonMovies);

                if (entityListFragmentFragment != null) {
                    for (Movie m: movies) {
                        entityListFragmentFragment.addMovie(m);
                    }

                    stopProgressBarAnimation();

                    entityListFragmentFragment.getView().setAlpha(0f);
                    entityListFragmentFragment.getView().animate().alpha(1f).setDuration(animationDelay);
                }
            } catch (Exception ex) {
                Log.d("Error", ex.getMessage(), ex);
            }
        }
    }

    private class RetrieveEntityTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            return new ApiRequester().get(params[0]);
        }

        protected void onPostExecute(String response) {
            try {
                Movie movie = new Mapper().mapToMovieEntity(response);

                if (likedListFragment != null) {
                    likedListFragment.addMovie(movie);
                    stopProgressBarAnimation();
                }
            } catch (Exception ex) {
                Log.d("Error", ex.getMessage(), ex);
            }
        }
    }
}
