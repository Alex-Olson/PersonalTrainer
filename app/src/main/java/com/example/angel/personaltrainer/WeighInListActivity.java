package com.example.angel.personaltrainer;


import android.app.Fragment;

public class WeighInListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new WeighInListFragment();
    }
}
