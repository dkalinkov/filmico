package com.ntropia.filmico.utilities;

import android.util.Log;

import com.ntropia.filmico.models.Movie;
import com.ntropia.filmico.models.MovieDetails;

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

    public MovieDetails[] mapToMovieDetails(String jsonInput) {
        MovieDetails[] moviesResult = null;
        try {
            JSONObject json = new JSONObject(jsonInput);
            JSONArray movies = json.getJSONArray("results");
            moviesResult = new MovieDetails[movies.length()];
            for (int i = 0; i < movies.length(); i++) {
                MovieDetails movie = new MovieDetails();
                JSONObject jObj = movies.optJSONObject(i);

                movie.refId = jObj.getString("id");
                movie.voteCount = jObj.getString("vote_count");
                movie.voteScore = jObj.getString("vote_average");
                movie.popularityIndex = jObj.getString("popularity");

                movie.runtime = jObj.getString("runtime");
                movie.budget = jObj.getString("budget");
                movie.revenue = jObj.getString("revenue");

                movie.title = jObj.getString("title");
                movie.description = jObj.getString("overview");
                movie.releaseDate = jObj.getString("release_date");
                movie.homepage = jObj.getString("homepage");
                movie.tagline = jObj.getString("tagline");
                movie.status = jObj.getString("status");

                JSONArray genres = jObj.getJSONArray("genres");
                movie.genres = new String[genres.length()];
                for (int j = 0; j < genres.length(); j++) {
                    JSONObject genreObj = genres.optJSONObject(j);
                    movie.genres[j] = genreObj.getString("name");
                }

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
