package com.ntropia.filmico.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.ntropia.filmico.R;
import com.ntropia.filmico.adapters.MoviesAdapter;
import com.ntropia.filmico.models.Movie;

import java.util.ArrayList;

public class LikedListFragment extends Fragment {
    private MoviesAdapter _moviesListAdapter;

    public LikedListFragment() { }

    public static LikedListFragment newInstance() {
        return new LikedListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currView = inflater.inflate(R.layout.fragment_liked_list, container, false);

        ArrayList<Movie> moviesList = new ArrayList<Movie>();
        _moviesListAdapter = new MoviesAdapter(getActivity().getBaseContext(), moviesList);

        final ListView moviesListView = (ListView) currView.findViewById(R.id.moviesLikedListVIew);
        moviesListView.setAdapter(_moviesListAdapter);

        String spKey = getString(R.string.app_shared_pref_key);
        final SharedPreferences sp = getActivity().getBaseContext().getSharedPreferences(spKey, getActivity().getBaseContext().MODE_PRIVATE);

        final Button button = (Button) currView.findViewById(R.id.clear_pref_data);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();

                Snackbar.make(v, "Liked cleared!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        return currView;
    }

    public void addMovie(Movie movie) {
        _moviesListAdapter.add(movie);
    }
}
