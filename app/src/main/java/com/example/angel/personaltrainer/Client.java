package com.example.angel.personaltrainer;


import java.util.ArrayList;
import java.util.UUID;

public class Client {
    private UUID mId;
    private String mName;
    private String mEmail;
    private ArrayList<WeighIn> mWeighIns;

    public Client(){
        mId = UUID.randomUUID();
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

    public ArrayList<WeighIn> getWeighIns() {
        return mWeighIns;
    }

    public void setWeighIns(ArrayList<WeighIn> a) {
        this.mWeighIns = a;
    }

    public void addWeighIn(WeighIn w){
        mWeighIns.add(w);
    }
}
