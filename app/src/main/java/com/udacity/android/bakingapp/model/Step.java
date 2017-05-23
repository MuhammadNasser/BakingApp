package com.udacity.android.bakingapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Step implements Serializable {

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;


    public Step(JSONObject stepJason) {
        try {
            this.id = stepJason.getInt("id");
            this.shortDescription = stepJason.optString("shortDescription");
            this.description = stepJason.optString("description");
            this.videoURL = stepJason.optString("videoURL");
            this.thumbnailURL = stepJason.getString("thumbnailURL");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}




