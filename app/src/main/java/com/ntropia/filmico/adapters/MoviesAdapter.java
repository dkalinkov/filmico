package com.ntropia.filmico.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntropia.filmico.R;
import com.ntropia.filmico.models.Movie;
import com.ntropia.filmico.utilities.ApiRequester;
import com.ntropia.filmico.utilities.UrlBulder;

import java.util.ArrayList;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    private ImageView _movieImage;

    public MoviesAdapter(Context context, ArrayList<Movie> resource) {
        super(context, R.layout.movie_list_row, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater li = LayoutInflater.from(getContext());
        View movieRowView = li.inflate(R.layout.movie_list_row, parent, false);

        Movie movie = getItem(position);
        if (movie != null) {
            TextView movieTitle = (TextView) movieRowView.findViewById(R.id.movieListRowTitle);
            TextView movieScore = (TextView) movieRowView.findViewById(R.id.movieListRowScore);
            TextView movieReleaseDate = (TextView) movieRowView.findViewById(R.id.movieListReleaseDate);
            ImageView movieImage = (ImageView) movieRowView.findViewById(R.id.movieListRowImage);

            _movieImage = movieImage;

            movieTitle.setText(movie.title);
            movieScore.setText(movie.voteScore);
            movieReleaseDate.setText(movie.releaseDate);
            movieImage.setTag(movie.posterUrl);

            new RetrieveImageTask().execute(movieImage);
        }

        return movieRowView;
    }

    class RetrieveImageTask extends AsyncTask<ImageView, Void, Bitmap> {
        String imageTag;
        ImageView imageView = _movieImage;

        @Override
        protected void onPreExecute() {
            imageTag = (String) imageView.getTag();
            imageView.setAlpha(0f);
        }

        protected Bitmap doInBackground(ImageView... params) {
            imageView = params[0];
            String imageUrl = UrlBulder.generatePosterImageUrl(imageTag, true);
            Bitmap image = new ApiRequester().getImage(imageUrl);
            return image;
        }

        protected void onPostExecute(Bitmap response) {
            imageView.setImageBitmap(response);
            imageView.animate().alpha(1f).setDuration(200);
        }
    }

}
