package com.example.angel.personaltrainer;


import android.app.Fragment;

public class CameraActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new CameraFragment();
    }

}
