package com.ntropia.filmico.utilities;

import android.util.Log;

import com.ntropia.filmico.models.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

public class Mapper {

    public Movie[] mapToMovie(String jsonInput) {
        Movie[] moviesResult = null;
        try {
            JSONObject json = new JSONObject(jsonInput);
            JSONArray movies = json.getJSONArray("results");
            moviesResult = new Movie[movies.length()];
            for (int i = 0; i < movies.length(); i++) {
                Movie movie = new Movie();
                JSONObject jObj = movies.optJSONObject(i);

                movie.refId = jObj.getString("id");
                movie.voteCount = jObj.getString("vote_count");
                movie.voteScore = jObj.getString("vote_average");
                movie.popularityIndex = jObj.getString("popularity");
                movie.title = jObj.getString("title");
                movie.description = jObj.getString("overview");
                movie.releaseDate = jObj.getString("release_date");
                movie.posterUrl = jObj.getString("poster_path");
                movie.backdropUrl = jObj.getString("backdrop_path");

                moviesResult[i] = movie;
            }
        } catch (Exception ex) {
            Log.d("Parsing error", ex.getMessage(), ex);
        }

        return moviesResult;
    }

}
