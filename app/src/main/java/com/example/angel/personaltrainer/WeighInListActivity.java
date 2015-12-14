package com.example.angel.personaltrainer;


import android.app.Fragment;
import java.util.UUID;

public class WeighInListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID clientId = UUID.fromString(getIntent().getSerializableExtra(WeighInListFragment.EXTRA_CLIENT_ID).toString());

        return WeighInListFragment.newInstance(clientId);
    }
}
