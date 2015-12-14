package com.example.angel.personaltrainer;


import android.app.Fragment;

import java.util.UUID;

public class ClientActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID clientId = UUID.fromString(getIntent().getSerializableExtra(ClientFragment.EXTRA_CLIENT_ID).toString());

        return ClientFragment.newInstance(clientId);
    }
}
