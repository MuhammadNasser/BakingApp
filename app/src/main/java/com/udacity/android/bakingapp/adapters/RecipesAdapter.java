package com.udacity.android.bakingapp.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.android.bakingapp.MainActivity;
import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.StepsActivity;
import com.udacity.android.bakingapp.model.Recipe;

import java.util.ArrayList;

import static com.udacity.android.bakingapp.MainActivity.KEY_RECIPE_ITEM;


public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final private ArrayList<Recipe> recipes;
    private MainActivity activity;
    private RecyclerView recyclerView;
    private LayoutInflater inflater;
    private boolean isTabletView;

    public RecipesAdapter(MainActivity activity, RecyclerView recyclerView, ArrayList<Recipe> recipes) {
        this.activity = activity;
        this.recyclerView = recyclerView;
        this.isTabletView = activity.isTabletView;
        inflater = activity.getLayoutInflater();
        this.recipes = recipes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;

        //inflater your layout and pass it to view holder
        view = inflater.inflate(R.layout.item_recipe, viewGroup, false);
        holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
        layoutParams.setFullSpan(false);

        if (isTabletView) {
            layoutParams.width = (int) (recyclerView.getMeasuredWidth() * 0.5);
        }

        ItemHolder itemHolder = (ItemHolder) viewHolder;
        itemHolder.setDetails(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }


    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        TextView textViewServings;
        ImageView imageView;
        Recipe recipe;

        private ItemHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewServings = (TextView) itemView.findViewById(R.id.textViewServings);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
        }

        private void setDetails(Recipe recipe) {
            this.recipe = recipe;
            textViewName.setText(recipe.getName());
            if (recipe.getImage().isEmpty()) {
                imageView.setImageResource(R.drawable.placeholder_recipe);
            } else {
                Picasso.with(activity).load(recipe.getImage()).
                        placeholder(R.drawable.placeholder_recipe).
                        error(R.drawable.warning).
                        into(imageView);
            }

            textViewServings.setText(activity.getString(R.string.servings) + ": " + recipe.getServings());
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                Intent intent = new Intent(activity, StepsActivity.class);
                intent.putExtra(KEY_RECIPE_ITEM, recipe);
                activity.startActivity(intent);
            }
        }
    }
}
