package com.example.angel.personaltrainer;



import org.json.JSONException;
import org.json.JSONObject;


import java.util.UUID;

public class Client {
    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";
    private static final String JSON_EMAIL = "email";


    private UUID mId;
    private String mName;
    private String mEmail;


    public Client(){
        mId = UUID.randomUUID();
    }
    public Client(JSONObject json) throws JSONException{
        mId = UUID.fromString(json.getString(JSON_ID));
       mName = json.getString(JSON_NAME);
        mEmail = json.getString(JSON_EMAIL);


    }
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JSON_ID, mId.toString());
        json.put(JSON_NAME, mName);
        json.put(JSON_EMAIL, mEmail);

        return json;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String s) {
        this.mName = s;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String s) {
        this.mEmail = s;
    }

}
