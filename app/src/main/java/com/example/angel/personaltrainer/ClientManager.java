package com.example.angel.personaltrainer;


import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ClientManager {

    //todo: split up json files for each client
    private static final String TAG = "ClientManager";
    private static final String FILENAME = "clients.json";
    private ArrayList<Client> mClients;
    private JSONSerializer mSerializer;

    private static ClientManager sClientManager;
    private Context mContext;

    private ClientManager(Context appContext){
        mContext = appContext;
    }

    public static ClientManager get(Context c){
        if (sClientManager == null){
            sClientManager = new ClientManager(c.getApplicationContext());
        }
        return sClientManager;
    }

    public ArrayList<Client> getClients(){
        return mClients;
    }

    public void addClient(Client c){
        mClients.add(c);
    }

    public Client getClient(UUID id){
        for (Client c : mClients){
            if (c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }
}
