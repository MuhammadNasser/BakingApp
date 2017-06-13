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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
        try {
            new RecipesAsyncTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("ExecutionException: ", String.valueOf(e));
        }
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

        views.removeAllViews(R.id.linearLayoutIngredients);
        for (int i = 0; i < ingredients.size(); i++) {
            RemoteViews ingredientViews = new RemoteViews(context.getPackageName(), R.layout.item_ingredient);
            views.addView(R.id.linearLayoutIngredients, ingredientViews);
            ingredientViews.setTextViewText(R.id.textViewQuantity, ingredients.get(i).getQuantity() + "");
            ingredientViews.setTextViewText(R.id.textViewMeasure, ingredients.get(i).getMeasure());
            ingredientViews.setTextViewText(R.id.textViewIngredient, ingredients.get(i).getIngredient());
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


    private class RecipesAsyncTask extends AsyncTask<Void, Void, ArrayList<Recipe>> {

        @Override
        protected ArrayList<Recipe> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Uri builtUri = Uri.parse(context.getString(R.string.URL))
                        .buildUpon()
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                JSONArray movieArray = new JSONArray(buffer.toString());
                recipes = new ArrayList<>();
                for (int i = 0; i < movieArray.length(); i++) {
                    recipes.add(new Recipe(movieArray.getJSONObject(i)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception ignored) {
                }
                return recipes;
            }
        }
    }


}
