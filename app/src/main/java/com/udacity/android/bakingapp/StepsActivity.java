package com.udacity.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.udacity.android.bakingapp.fragments.StepsDetailsFragment;
import com.udacity.android.bakingapp.fragments.StepsFragment;
import com.udacity.android.bakingapp.model.Recipe;

import static com.udacity.android.bakingapp.MainActivity.KEY_RECIPE_ITEM;
import static com.udacity.android.bakingapp.MainActivity.isTabletView;

/**
 * Created by Muhammad on 5/15/2017
 */

public class StepsActivity extends AppCompatActivity {

    private TextView textViewTitle;
    public Recipe recipeItem;
    public int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        recipeItem = (Recipe) getIntent().getSerializableExtra(KEY_RECIPE_ITEM);

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.stepsDetailsContainer) != null) {

            isTabletView = true;
            StepsFragment stepsFragment = new StepsFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.stepsContainer, stepsFragment)
                    .commit();

            Bundle bundle = new Bundle();
            bundle.putSerializable(KEY_RECIPE_ITEM, recipeItem);
            bundle.putInt("index",index);

            StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
            stepsDetailsFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.stepsDetailsContainer, stepsDetailsFragment)
                    .commit();
        } else {
            StepsFragment stepsFragment = new StepsFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.stepsContainer, stepsFragment)
                    .commit();
        }
        setToolBar();
    }


    private void setToolBar() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);

        textViewTitle = (TextView) findViewById(R.id.tv_title);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void setActivityTitle(String title) {
        if (textViewTitle != null) {
            textViewTitle.setText(title);
        }
    }
}

