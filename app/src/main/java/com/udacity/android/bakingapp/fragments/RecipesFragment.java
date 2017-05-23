package com.udacity.android.bakingapp.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.android.bakingapp.MainActivity;
import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.adapters.RecipesAdapter;
import com.udacity.android.bakingapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RecipesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MainActivity activity;

    public RecipesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        activity = (MainActivity) getActivity();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        new RecipesAsyncTask().execute();
        return view;
    }

    private class RecipesAsyncTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

        private final String LOG_TAG = RecipesAsyncTask.class.getCanonicalName();

        /**
         * {@link java.lang.reflect.Constructor}
         */
        private RecipesAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.isLoading(true);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String recipesJsonStr = null;
            try {
                URL url = getApiUrl();

                // Start connecting to get JSON
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Adds '\n' at last line if not already there.
                    // This supposedly makes it easier to debug.
                    builder.append(line).append("");
                }
                if (builder.length() == 0) {
                    return null;
                }
                recipesJsonStr = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "error", e);
                return null;
            } finally {
                // Tidy up: release url connection and buffered reader
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "close", e);
                    }
                }
            }
            try {
                return getrecipesDataFromJson(recipesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        /**
         * Extracts data from the JSON object and returns an Array of recipe objects.
         *
         * @param recipesJsonStr JSON string to be traversed
         * @return ArrayList of Recipe objects
         * @throws JSONException throws JSONException
         */
        private ArrayList<Recipe> getrecipesDataFromJson(String recipesJsonStr) throws JSONException {

            JSONArray resultsArray = new JSONArray(recipesJsonStr);

            ArrayList<Recipe> recipes = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject recipeInfo = resultsArray.getJSONObject(i);
                //
                Recipe recipeItem = new Recipe(recipeInfo);
                recipes.add(recipeItem);
            }
            return recipes;
        }

        /**
         * Creates and returns an URL.
         *
         * @return URL formatted with parameters for the API
         * @throws MalformedURLException throws MalformedURLException
         */
        private URL getApiUrl() throws MalformedURLException {
            final String API_BASE_URL = activity.getString(R.string.URL);
            Uri builtUri;
            builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .build();
            return new URL(builtUri.toString());
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            activity.isLoading(false);
            if (recipes != null) {
                //setting recyclerView layout and adapter.
                StaggeredGridLayoutManager layoutManager1;
                if (activity.isTabletView) {
                    layoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                } else {
                    layoutManager1 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                }
                layoutManager1.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                recyclerView.setLayoutManager(layoutManager1);
                recyclerView.setHasFixedSize(false);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.setAdapter(new RecipesAdapter(activity, recyclerView, recipes));
            }
        }
    }
}
