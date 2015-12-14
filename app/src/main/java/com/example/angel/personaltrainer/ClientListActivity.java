package com.example.angel.personaltrainer;


import android.app.Fragment;

public class ClientListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new ClientListFragment();
    }

}
