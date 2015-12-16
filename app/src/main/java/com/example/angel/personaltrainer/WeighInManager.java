package com.example.angel.personaltrainer;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class WeighInManager
// singleton for managing weighins
{

    private static final String TAG = "WeighInManager";
    //I tried to split up the json files for weigh-ins by client by setting the file name to the UUID of the client, but it still always returns the
    //one json file. I circumvented that in the adapter view but left the code here to prevent anything from exploding this close to the
    //deadline.
    private static String mFilename;
    private ArrayList<WeighIn> mWeighIns;
    private JSONSerializer mSerializer;

    private static WeighInManager sWeighInManager;
    private Context mContext;
    private static UUID currentId;


    private WeighInManager(Context appContext, UUID clientId){

        mContext = appContext;

        mSerializer = new JSONSerializer(mContext, mFilename);

        try{
            mWeighIns = mSerializer.loadWeighIns();
        } catch (Exception e){
            Log.e(TAG, "Error loading weigh-ins: ", e);
        }
    }

    public static WeighInManager get(Context c, UUID clientId){
        mFilename = clientId.toString() + "weighIns.json";

       if (sWeighInManager == null){
            sWeighInManager = new WeighInManager(c.getApplicationContext(), clientId);
        }

        return sWeighInManager;
    }

    public void addWeighIn(WeighIn w){
        mWeighIns.add(w);
        saveWeighIns();
    }

    public void deleteWeighIn(WeighIn w){
        mWeighIns.remove(w);
        saveWeighIns();
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
