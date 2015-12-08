package com.example.angel.personaltrainer;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class WeighInManager {
    //todo: split up each clients' weigh in files
    private static final String TAG = "WeighInManager";
    private static final String FILENAME =  "weighIns.json";
    private ArrayList<WeighIn> mWeighIns;
    private JSONSerializer mSerializer;

    private static WeighInManager sWeighInManager;
    private Context mContext;


    private WeighInManager(Context appContext){
        mContext = appContext;

        mSerializer = new JSONSerializer(mContext, FILENAME);

        try{
            mWeighIns = mSerializer.loadWeighIns();
        } catch (Exception e){
            Log.e(TAG, "Error loading weigh-ins: ", e);
        }
    }

    public static WeighInManager get(Context c){
        if (sWeighInManager == null){
            sWeighInManager = new WeighInManager(c.getApplicationContext());
        }
        return sWeighInManager;
    }

    public void addWeighIn(WeighIn w){
        mWeighIns.add(w);
    }

    public boolean saveWeighIns(){
        try{
            mSerializer.saveWeighIns(mWeighIns);
            Log.d(TAG, "weigh-ins saved to file");
            return true;
        } catch (Exception e){
            Log.e(TAG, "Error saving weigh-ins: ", e);
            return false;
        }
    }

    public ArrayList<WeighIn> getWeighIns(){
        return mWeighIns;
    }

    public WeighIn getWeighIn(Date date){
        for (WeighIn w : mWeighIns){
            if (w.getDate().equals(date)){
                return w;
            }
        }
        return null;
    }
}
