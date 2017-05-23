package com.udacity.android.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.StepsActivity;
import com.udacity.android.bakingapp.adapters.StepsAdapter;
import com.udacity.android.bakingapp.model.Ingredient;
import com.udacity.android.bakingapp.model.Recipe;
import com.udacity.android.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * Created by Muhammad on 5/15/2017
 */

public class StepsFragment extends Fragment {

    private LinearLayout linearLayoutIngredients;
    private RecyclerView recyclerView;

    StepsActivity activity;
    Recipe recipeItem;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        linearLayoutIngredients = (LinearLayout) view.findViewById(R.id.linearLayoutIngredients);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSteps);

        activity = (StepsActivity) getActivity();
        recipeItem = activity.recipeItem;
        ingredients = recipeItem.getIngredients();
        steps = recipeItem.getSteps();

        setIngredientsView(ingredients, inflater);
        setStepsView(steps);


        activity.setActivityTitle(recipeItem.getName());
        return view;
    }


    private void setIngredientsView(ArrayList<Ingredient> ingredients, LayoutInflater inflater) {
        if (ingredients != null) {
            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ingredients.get(i);
                View ingredientItem = inflater.inflate(R.layout.item_ingredient, null);
                linearLayoutIngredients.addView(ingredientItem);

                TextView textViewQuantity = (TextView) ingredientItem.findViewById(R.id.textViewQuantity);
                TextView textViewMeasure = (TextView) ingredientItem.findViewById(R.id.textViewMeasure);
                TextView textViewIngredient = (TextView) ingredientItem.findViewById(R.id.textViewIngredient);

                textViewIngredient.setText(ingredient.getIngredient());
                textViewMeasure.setText(ingredient.getMeasure());
                textViewQuantity.setText(String.valueOf(ingredient.getQuantity()));
            }
        }
    }

    private void setStepsView(ArrayList<Step> steps) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new StepsAdapter(activity, steps,recipeItem));
    }
}
