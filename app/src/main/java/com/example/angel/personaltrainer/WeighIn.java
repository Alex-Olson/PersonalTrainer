package com.example.angel.personaltrainer;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class WeighIn {

    private static final String JSON_PHOTO = "photo";
    private static final String JSON_WEIGHT = "weight";
    private static final String JSON_DATE = "date";

    private Photo mPhoto;
    private int mWeight;
    private Date mDate;

    public WeighIn(){
        mDate = new Date();
    }

    public WeighIn(JSONObject json) throws JSONException{
        mDate = new Date(json.getLong(JSON_DATE));
        if (json.has(JSON_PHOTO))
        {
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));

        }
        mWeight = json.getInt(JSON_WEIGHT);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();

        json.put(JSON_WEIGHT, mWeight);
        json.put(JSON_DATE, mDate.getTime());
        if (mPhoto != null)
            json.put(JSON_PHOTO, mPhoto.toJSON());
        return json;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int mWeight) {
        this.mWeight = mWeight;
    }

    public Date getDate() {
        return mDate;
    }




}
