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

import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.StepsActivity;
import com.udacity.android.bakingapp.adapters.IngredientAdapter;
import com.udacity.android.bakingapp.adapters.StepsAdapter;
import com.udacity.android.bakingapp.model.Ingredient;
import com.udacity.android.bakingapp.model.Recipe;
import com.udacity.android.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * Created by Muhammad on 5/15/2017
 */

public class StepsFragment extends Fragment {

    private RecyclerView recyclerViewIngredients;
    private RecyclerView recyclerViewSteps;

    StepsActivity activity;
    Recipe recipeItem;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        recyclerViewIngredients = (RecyclerView) view.findViewById(R.id.recyclerViewIngredients);
        recyclerViewSteps = (RecyclerView) view.findViewById(R.id.recyclerViewSteps);

        activity = (StepsActivity) getActivity();
        recipeItem = activity.recipeItem;
        ingredients = recipeItem.getIngredients();
        steps = recipeItem.getSteps();

        setIngredientsView(ingredients);
        setStepsView(steps);


        activity.setActivityTitle(recipeItem.getName());
        return view;
    }


    private void setIngredientsView(ArrayList<Ingredient> ingredients) {
        if (ingredients != null) {
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerViewIngredients.setLayoutManager(layoutManager);
            recyclerViewIngredients.setHasFixedSize(false);
            recyclerViewIngredients.setItemAnimator(new DefaultItemAnimator());

            recyclerViewIngredients.setAdapter(new IngredientAdapter(activity, ingredients));
        }
    }

    private void setStepsView(ArrayList<Step> steps) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerViewSteps.setLayoutManager(layoutManager);
        recyclerViewSteps.setHasFixedSize(false);
        recyclerViewSteps.setItemAnimator(new DefaultItemAnimator());

        recyclerViewSteps.setAdapter(new StepsAdapter(activity, steps, recipeItem));
    }
}
