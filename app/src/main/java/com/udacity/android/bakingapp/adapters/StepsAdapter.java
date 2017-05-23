package com.udacity.android.bakingapp.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.StepsActivity;
import com.udacity.android.bakingapp.StepsDetailsActivity;
import com.udacity.android.bakingapp.fragments.StepsDetailsFragment;
import com.udacity.android.bakingapp.model.Recipe;
import com.udacity.android.bakingapp.model.Step;

import java.util.ArrayList;

import static com.udacity.android.bakingapp.MainActivity.KEY_RECIPE_ITEM;
import static com.udacity.android.bakingapp.MainActivity.isTabletView;


public class StepsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private ArrayList<Step> steps;
    private StepsActivity activity;
    private Recipe recipeItem;

    private LayoutInflater inflater;

    public StepsAdapter(StepsActivity activity, ArrayList<Step> steps, Recipe recipeItem) {
        this.activity = activity;
        this.recipeItem = recipeItem;
        inflater = activity.getLayoutInflater();
        this.steps = steps;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;

        //inflater your layout and pass it to view holder
        view = inflater.inflate(R.layout.item_step, viewGroup, false);
        holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
        layoutParams.setFullSpan(false);

        ItemHolder itemHolder = (ItemHolder) viewHolder;
        itemHolder.setDetails(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }


    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewStepTitle;
        ImageView imageView;
        Step step;

        private ItemHolder(View itemView) {
            super(itemView);
            textViewStepTitle = (TextView) itemView.findViewById(R.id.textViewStepTitle);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
        }

        private void setDetails(Step step) {
            this.step = step;
            int stepNum = step.getId();

            if (stepNum == 0) {
                textViewStepTitle.setText(step.getShortDescription());
            } else {
                textViewStepTitle.setText(stepNum + "- " + step.getShortDescription() + "");
            }

            if (step.getThumbnailURL().isEmpty()) {
                imageView.setImageResource(R.drawable.placeholder);
            } else {
                Picasso.with(activity).load(step.getThumbnailURL()).
                        placeholder(R.drawable.placeholder).
                        error(R.drawable.warning).
                        into(imageView);
            }
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (isTabletView) {
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(KEY_RECIPE_ITEM, recipeItem);
                    bundle.putInt("index", step.getId());

                    StepsDetailsFragment stepsDetailsFragment = new StepsDetailsFragment();
                    stepsDetailsFragment.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.stepsDetailsContainer, stepsDetailsFragment)
                            .commit();
                } else {
                    Intent intent = new Intent(activity, StepsDetailsActivity.class);
                    intent.putExtra("index", step.getId());
                    intent.putExtra(KEY_RECIPE_ITEM, recipeItem);
                    activity.startActivity(intent);
                }
            }
        }
    }
}
