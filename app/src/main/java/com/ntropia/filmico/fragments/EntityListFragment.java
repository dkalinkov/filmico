package com.ntropia.filmico.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ntropia.filmico.R;
import com.ntropia.filmico.adapters.MoviesAdapter;
import com.ntropia.filmico.models.Movie;

import java.util.ArrayList;

public class EntityListFragment extends Fragment {
    private MoviesAdapter moviesListAdapter;

    public EntityListFragment() { }

    public static EntityListFragment newInstance() {
        return new EntityListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View currView = inflater.inflate(R.layout.fragment_landing_screen, container, false);

        ArrayList<Movie> moviesList = new ArrayList<Movie>();
        moviesListAdapter = new MoviesAdapter(getActivity().getBaseContext(), moviesList);

        final ListView moviesListView = (ListView) currView.findViewById(R.id.moviesListVIew);
        moviesListView.setAdapter(moviesListAdapter);

        return currView;
    }

    public void addMovie(Movie movie) {
        this.moviesListAdapter.add(movie);
    }
}
