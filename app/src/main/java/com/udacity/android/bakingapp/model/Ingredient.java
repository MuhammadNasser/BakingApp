package com.udacity.android.bakingapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Ingredient implements Serializable {

    private double quantity;
    private String measure;
    private String ingredient;

    public Ingredient(JSONObject ingredientJason) {
        try {
            this.quantity = ingredientJason.getDouble("quantity");
            this.measure = ingredientJason.optString("measure");
            this.ingredient = ingredientJason.optString("ingredient");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}




