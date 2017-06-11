package com.udacity.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Muhammad on 5/15/2017
 * * Creates a Ingredient object.
 * The Parcelable is generated to transfer object by Intents.
 */

public class Ingredient implements Parcelable {

    private double quantity;
    private String measure;
    private String ingredient;

    /**
     * Constructor for a Ingredient object it's fill data in Strings from JSONObject.
     */
    Ingredient(JSONObject ingredientJason) {
        this.quantity = ingredientJason.optDouble("quantity");
        this.measure = ingredientJason.optString("measure");
        this.ingredient = ingredientJason.optString("ingredient");
    }

    /**
     * Gets the data that's added in Strings.
     *
     * @return Strings data
     */

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }


    private Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}




