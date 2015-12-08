package com.example.angel.personaltrainer;


import android.app.Fragment;

public class ClientActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new ClientFragment();
    }
}
