package com.udacity.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Muhammad on 5/15/2017
 * * Creates a Recipe object.
 * The Parcelable is generated to transfer object by Intents.
 */

public class Recipe implements Parcelable {

    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private String servings;
    private String image;

    /**
     * Constructor for a Recipe object it's fill data in Strings from JSONObject.
     */
    public Recipe(JSONObject recipeJason) {
        this.name = recipeJason.optString("name");
        this.image = recipeJason.optString("image");
        this.ingredients = new ArrayList<>();
        JSONArray ingredientsJA = recipeJason.optJSONArray("ingredients");
        for (int i = 0; i < ingredientsJA.length(); i++) {
            ingredients.add(new Ingredient(ingredientsJA.optJSONObject(i)));
        }
        this.steps = new ArrayList<>();
        JSONArray stepsJA = recipeJason.optJSONArray("steps");
        for (int i = 0; i < stepsJA.length(); i++) {
            steps.add(new Step(stepsJA.optJSONObject(i)));
        }
        this.servings = recipeJason.optString("servings");
    }

    /**
     * Gets the data that's added in Strings.
     *
     * @return Strings data
     */

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getServings() {
        return servings;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        servings = in.readString();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeString(servings);
        parcel.writeString(image);
    }
}




