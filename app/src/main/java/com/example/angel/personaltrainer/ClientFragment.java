package com.example.angel.personaltrainer;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Date;
import java.util.UUID;

public class ClientFragment extends Fragment {
    private Client mClient;
    private EditText mNameDisplay;
    private EditText mEmailDisplay;

    public static final String EXTRA_CLIENT_ID = "client fragment extra client id";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID id  = (UUID)getArguments().getSerializable(EXTRA_CLIENT_ID);
        mClient = ClientManager.get(getActivity()).getClient(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_weighin, parent, false);

        mNameDisplay = (EditText)v.findViewById(R.id.client_name);
        mNameDisplay.setText(mClient.getName());
        mNameDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mClient.setName(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mEmailDisplay = (EditText)v.findViewById(R.id.client_email);
        mEmailDisplay.setText(mClient.getEmail());
        mEmailDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mClient.setEmail(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }
}
