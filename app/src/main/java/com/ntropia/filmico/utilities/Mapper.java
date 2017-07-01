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

    public Movie mapToMovieEntity(String jsonInput) {
        MovieDetails movieDetails = mapToMovieDetails(jsonInput);

        Movie movie = new Movie();
        movie.refId = movieDetails.refId;
        movie.voteCount = movieDetails.voteCount;
        movie.voteScore = movieDetails.voteScore;
        movie.popularityIndex = movieDetails.popularityIndex;
        movie.title = movieDetails.title;
        movie.description = movieDetails.description;
        movie.releaseDate = movieDetails.releaseDate;
        movie.posterUrl = movieDetails.posterUrl;
        movie.backdropUrl = movieDetails.backdropUrl;

        return movie;
    }

    public MovieDetails mapToMovieDetails(String jsonInput) {
        MovieDetails movie = new MovieDetails();
        try {
            JSONObject json = new JSONObject(jsonInput);

            movie.refId = json.getString("id");
            movie.voteCount = json.getString("vote_count");
            movie.voteScore = json.getString("vote_average");
            movie.popularityIndex = json.getString("popularity");

            movie.runtime = json.getString("runtime");
            movie.budget = json.getString("budget");
            movie.revenue = json.getString("revenue");

            movie.title = json.getString("title");
            movie.description = json.getString("overview");
            movie.releaseDate = json.getString("release_date");
            movie.homepage = json.getString("homepage");
            movie.tagline = json.getString("tagline");
            movie.status = json.getString("status");

            JSONArray genres = json.getJSONArray("genres");
            movie.genres = new String[genres.length()];
            for (int j = 0; j < genres.length(); j++) {
                JSONObject genreObj = genres.optJSONObject(j);
                movie.genres[j] = genreObj.getString("name");
            }

            movie.posterUrl = json.getString("poster_path");
            movie.backdropUrl = json.getString("backdrop_path");
        } catch (Exception ex) {
            Log.d("Parsing error", ex.getMessage(), ex);
        }

        return movie;
    }
}
