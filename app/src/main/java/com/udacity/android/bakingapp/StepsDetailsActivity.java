package com.udacity.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.udacity.android.bakingapp.fragments.StepsDetailsFragment;
import com.udacity.android.bakingapp.model.Recipe;
import com.udacity.android.bakingapp.model.Step;

import java.util.ArrayList;

import static com.udacity.android.bakingapp.MainActivity.KEY_RECIPE_ITEM;

/**
 * Created by Muhammad on 5/20/2017
 */

public class StepsDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewNext;
    private TextView textViewPrevious;
    private TextView textViewTitle;
    private ArrayList<Step> steps;
    private Recipe recipeItem;
    private int index;
    private String name;

    FragmentManager fragmentManager;
    StepsDetailsFragment stepsDetailsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", 0);
        recipeItem = (Recipe) intent.getSerializableExtra(KEY_RECIPE_ITEM);
        steps = recipeItem.getSteps();
        name = recipeItem.getName();

        textViewNext = (TextView) findViewById(R.id.textViewNext);
        textViewPrevious = (TextView) findViewById(R.id.textViewPrevious);

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_RECIPE_ITEM, recipeItem);
        bundle.putInt("index", index);

        stepsDetailsFragment = new StepsDetailsFragment();
        stepsDetailsFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.stepsContainer, stepsDetailsFragment)
                .commit();

        textViewNext.setOnClickListener(this);
        textViewPrevious.setOnClickListener(this);

        if (index > 0) {
            textViewPrevious.setVisibility(View.VISIBLE);
        } else {
            textViewPrevious.setVisibility(View.GONE);
        }
        setToolBar();
    }

    @Override
    public void onClick(View v) {
        Bundle bundle;
        if (v == textViewNext) {
            if (index < steps.size() - 1) {
                index++;
                textViewPrevious.setVisibility(View.VISIBLE);

                bundle = new Bundle();
                bundle.putSerializable(KEY_RECIPE_ITEM, recipeItem);
                bundle.putInt("index", index);

                stepsDetailsFragment = new StepsDetailsFragment();
                stepsDetailsFragment.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.stepsContainer, stepsDetailsFragment)
                        .commit();
            }

        } else if (v == textViewPrevious) {
            if (index > 0) {
                index--;
                textViewPrevious.setVisibility(View.VISIBLE);

                bundle = new Bundle();
                bundle.putSerializable(KEY_RECIPE_ITEM, recipeItem);
                bundle.putInt("index", index);

                stepsDetailsFragment = new StepsDetailsFragment();
                stepsDetailsFragment.setArguments(bundle);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.stepsContainer, stepsDetailsFragment)
                        .commit();
            } else {
                textViewPrevious.setVisibility(View.GONE);
            }
        }
    }

    private void setToolBar() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);

        textViewTitle = (TextView) findViewById(R.id.tv_title);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setActivityTitle(name);

    }

    public void setActivityTitle(String title) {
        if (textViewTitle != null) {
            textViewTitle.setText(title);
        }
    }
}
