package com.udacity.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by Muhammad on 5/15/2017
 * * Creates a Step object.
 * The Parcelable is generated to transfer object by Intents.
 */

public class Step implements Parcelable {

    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;


    /**
     * Constructor for a Step object it's fill data in Strings from JSONObject.
     */

    Step(JSONObject stepJason) {
        this.id = stepJason.optInt("id");
        this.shortDescription = stepJason.optString("shortDescription");
        this.description = stepJason.optString("description");
        this.videoURL = stepJason.optString("videoURL");
        this.thumbnailURL = stepJason.optString("thumbnailURL");
    }

    /**
     * Gets the data that's added in Strings.
     *
     * @return Strings data
     */

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



    private Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }
}




