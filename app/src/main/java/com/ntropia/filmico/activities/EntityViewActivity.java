package com.ntropia.filmico.activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ntropia.filmico.R;
import com.ntropia.filmico.models.MovieDetails;
import com.ntropia.filmico.utilities.ApiRequester;
import com.ntropia.filmico.utilities.Mapper;
import com.ntropia.filmico.utilities.UrlBulder;

public class EntityViewActivity extends AppCompatActivity {

    private MovieDetails _movieDetails;
    private ImageView _entityImage;
    private CollapsingToolbarLayout _toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.entity_details_toolbar);
        setSupportActionBar(toolbar);

        _toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.entity_details_toolbar_layout);

        _entityImage = (ImageView) findViewById(R.id.entityBackdropImage);

        Bundle extras = getIntent().getExtras();
        String refId = extras.getString("entityRefId");
        String backdropUrl = extras.getString("entityBackdropImageUrl");

        getEntityData(refId, backdropUrl);
        configureFloatingActionBtn();
    }

    private void getEntityData(String refId, String backdropUrl) {
        _entityImage.setTag(backdropUrl);
        new RetrieveEntityImageTask().execute(_entityImage);

        String entityDetailsUrl = UrlBulder.generateUrlAddress(getString(R.string.api_url),
                getString(R.string.api_key),
                getString(R.string.api_movie),
                refId,
                null,
                null);
        new RetrieveEntityDetailsTask().execute(entityDetailsUrl);
    }

    private void configureFloatingActionBtn() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLikeClick(view);
            }
        });
    }

    private void onLikeClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private class RetrieveEntityDetailsTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            return new ApiRequester().get(params[0]);
        }

        protected void onPostExecute(String response) {
            try {
                _movieDetails = new Mapper().mapToMovieDetails(response);
                _toolbarLayout.setTitle(_movieDetails.title);
            } catch (Exception ex) {
                Log.d("Error", ex.getMessage(), ex);
            }
        }
    }

    private class RetrieveEntityImageTask extends AsyncTask<ImageView, Void, Bitmap> {
        String imageTag;
        ImageView imageView = _entityImage;

        @Override
        protected void onPreExecute() {
            imageTag = (String) imageView.getTag();
            imageView.setAlpha(0f);
        }

        protected Bitmap doInBackground(ImageView... params) {
            imageView = params[0];
            String imageUrl = UrlBulder.generatePosterImageUrl(imageTag, false);
            return new ApiRequester().getImage(imageUrl);
        }

        protected void onPostExecute(Bitmap response) {
            imageView.setImageBitmap(response);
            imageView.animate().alpha(1f).setDuration(200);
        }
    }
}
