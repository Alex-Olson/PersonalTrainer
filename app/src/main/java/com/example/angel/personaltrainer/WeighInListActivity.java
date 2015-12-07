package com.example.angel.personaltrainer;


import android.app.Fragment;
import android.view.MenuItem;

public class WeighInListActivity extends SingleFragmentActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected Fragment createFragment(){
        return new WeighInListFragment();
    }
}
