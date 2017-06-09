package com.udacity.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.model.Ingredient;
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

import static com.udacity.android.bakingapp.MainActivity.KEY_RECIPE_ITEM;
import static com.udacity.android.bakingapp.fragments.RecipesFragment.recipes;


public class WidgetRemoteViewsFactory implements RemoteViewsFactory {
    private Context context;

    public WidgetRemoteViewsFactory(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        new RecipesAsyncTask().execute();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Recipe recipe = recipes.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        try {
            views.setImageViewBitmap(R.id.imageView, BitmapFactory.decodeStream(new URL(recipe.getImage()).
                    openConnection().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        views.setTextViewText(R.id.textViewName, recipe.getName());
        views.setTextViewText(R.id.textViewServings, context.getString(R.string.servings) + ": " + recipe.getServings());

        ArrayList<Ingredient> ingredients = recipe.getIngredients();

        for (int i = 0; i < ingredients.size(); i++) {
            RemoteViews ingredientViews = new RemoteViews(context.getPackageName(), R.layout.item_ingredient);
            ingredientViews.setTextViewText(R.id.textViewQuantity, ingredients.get(i).getQuantity() + "");
            ingredientViews.setTextViewText(R.id.textViewMeasure, ingredients.get(i).getMeasure());
            ingredientViews.setTextViewText(R.id.textViewIngredient, ingredients.get(i).getIngredient());
            views.addView(R.id.linearLayoutIngredients, ingredientViews);
        }

        Intent intent = new Intent();
        intent.putExtra(KEY_RECIPE_ITEM, recipe);
        views.setOnClickFillInIntent(R.id.relativeLayout, intent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private class RecipesAsyncTask extends AsyncTask<String, Void, ArrayList<Recipe>> {

        private final String LOG_TAG = WidgetRemoteViewsFactory.class.getCanonicalName();

        /**
         * {@link java.lang.reflect.Constructor}
         */
        private RecipesAsyncTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

            recipes = new ArrayList<>();
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
            final String API_BASE_URL = context.getString(R.string.URL);
            Uri builtUri;
            builtUri = Uri.parse(API_BASE_URL).buildUpon()
                    .build();
            return new URL(builtUri.toString());
        }


    }


}
