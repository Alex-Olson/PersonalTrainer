package com.example.angel.personaltrainer;


import android.content.Context;

import java.util.ArrayList;

public class ClientManager {
    private static final String TAG = "ClientManager";
    private static final String FILENAME = "clients.json";
    private ArrayList<Client> clients;
    private JSONSerializer mSerializer;

    private static ClientManager sClientManager;
    private Context mContext;
}
