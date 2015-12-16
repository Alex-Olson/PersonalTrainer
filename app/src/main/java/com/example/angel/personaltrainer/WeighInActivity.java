package com.example.angel.personaltrainer;


import android.app.Fragment;


import java.util.Date;
import java.util.UUID;

public class WeighInActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Date weighInDate = (Date)getIntent().getSerializableExtra(WeighInFragment.EXTRA_WEIGHIN_DATE);
        UUID clientId = UUID.fromString(getIntent().getSerializableExtra(WeighInFragment.EXTRA_CLIENT_ID).toString());
        return WeighInFragment.newInstance(weighInDate, clientId);
    }
}
