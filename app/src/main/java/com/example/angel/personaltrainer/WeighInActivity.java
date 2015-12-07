package com.example.angel.personaltrainer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

public class WeighInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Date weighInDate = (Date)getIntent().getSerializableExtra(WeighInFragment.EXTRA_WEIGHIN_DATE);
        
        return WeighInFragment.newInstance(weighInDate);
    }
}
