package com.udacity.android.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.StepsActivity;
import com.udacity.android.bakingapp.model.Ingredient;

import java.util.ArrayList;


public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Ingredient> ingredients;


    private LayoutInflater inflater;

    public IngredientAdapter(StepsActivity activity, ArrayList<Ingredient> ingredients) {

        inflater = activity.getLayoutInflater();
        this.ingredients = ingredients;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;

        //inflater your layout and pass it to view holder
        view = inflater.inflate(R.layout.item_ingredient, viewGroup, false);
        holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) viewHolder.itemView.getLayoutParams();
        layoutParams.setFullSpan(false);

        ItemHolder itemHolder = (ItemHolder) viewHolder;
        itemHolder.setDetails(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView textViewQuantity;
        TextView textViewMeasure;
        TextView textViewIngredient;
        Ingredient ingredient;

        private ItemHolder(View itemView) {
            super(itemView);

            textViewQuantity = (TextView) itemView.findViewById(R.id.textViewQuantity);
            textViewMeasure = (TextView) itemView.findViewById(R.id.textViewMeasure);
            textViewIngredient = (TextView) itemView.findViewById(R.id.textViewIngredient);
        }

        private void setDetails(Ingredient ingredient) {
            this.ingredient = ingredient;

            textViewIngredient.setText(ingredient.getIngredient());
            textViewMeasure.setText(ingredient.getMeasure());
            textViewQuantity.setText(String.valueOf(ingredient.getQuantity()));
        }

    }
}
