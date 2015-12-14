package com.example.angel.personaltrainer;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class WeighIn {

    private static final String JSON_PHOTO = "photo";
    private static final String JSON_WEIGHT = "weight";
    private static final String JSON_DATE = "date";
    private static final String JSON_CLIENT = "client";

    private Photo mPhoto;
    private int mWeight;
    private Date mDate;

    public UUID getClientId() {
        return mClientId;
    }

    public void setClientId(UUID mClientId) {
        this.mClientId = mClientId;
    }

    private UUID mClientId;

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
        mClientId = UUID.fromString(json.getString(JSON_CLIENT));
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();

        json.put(JSON_WEIGHT, mWeight);
        json.put(JSON_DATE, mDate.getTime());
        if (mPhoto != null)
            json.put(JSON_PHOTO, mPhoto.toJSON());
        json.put(JSON_CLIENT, mClientId.toString());
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
