package com.udacity.android.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.android.bakingapp.fragments.RecipesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Muhammad on 5/13/2017
 */

public class MainActivity extends AppCompatActivity {

    public static final String KEY_RECIPE_ITEM = "recipe_item";

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_title)
    TextView textViewTitle;

    public static boolean isTabletView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipesFragment recipesFragment = new RecipesFragment();

        if (findViewById(R.id.frameLayoutTablet) != null) {
            isTabletView = true;
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayoutTablet, recipesFragment)
                    .commit();
        } else {
            isTabletView = false;
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, recipesFragment)
                    .commit();
        }

        setToolBar();
    }

    private void setToolBar() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);

        setActivityTitle(R.string.app_name);
    }

    public void setActivityTitle(int title) {
        if (textViewTitle != null) {
            textViewTitle.setText(title);
        }
    }

    public void isLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

}
