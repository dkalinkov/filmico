package com.ntropia.filmico.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ntropia.filmico.R;
import com.ntropia.filmico.activities.EntityViewActivity;
import com.ntropia.filmico.models.Movie;
import com.ntropia.filmico.utilities.ApiRequester;
import com.ntropia.filmico.utilities.Mapper;
import com.ntropia.filmico.utilities.UrlBulder;

import java.util.HashMap;

public class SearchFragment extends Fragment {
    private EditText _searchBarText;

    public SearchFragment() { }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.fragment_search, container, false);

        Button searchBtn = (Button) inflated.findViewById(R.id.btn_submit_search);
        _searchBarText = (EditText) inflated.findViewById(R.id.entity_search_input);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputString = _searchBarText.getText().toString();
                if (inputString.length() > 0){
                    HashMap<String, String> queryParams = new HashMap<>();
                    queryParams.put("query", inputString);
                    String url = UrlBulder.generateUrlAddress(getString(R.string.api_url),
                            getString(R.string.api_key),
                            getString(R.string.api_search_movies), queryParams);

                    new RetrieveSearchQueryTask().execute(url);
                } else{
                    final Toast toast = Toast.makeText(getContext(), "Enter some text", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        return inflated;
    }

    class RetrieveSearchQueryTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            return new ApiRequester().get(params[0]);
        }

        protected void onPostExecute(String response) {
            try {
                Movie[] movies = new Mapper().mapToMovie(response);
                Movie movie = movies[0];

                Intent intent = new Intent(getContext(), EntityViewActivity.class);
                intent.putExtra("entityRefId", movie.refId);
                intent.putExtra("entityBackdropImageUrl", movie.backdropUrl);
                startActivity(intent);
            } catch (Exception ex){
                Log.d("Error", ex.getMessage(), ex);
            }
        }
    }
}
