package com.example.angel.personaltrainer;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class WeighInFragment extends Fragment
//Edit data about the weigh-in. pretty straightforward. can take a picture, save it + send it using the email of the client.
{
    private WeighIn mWeighIn;

    private ImageView mPhotoDisplay;
    private ImageButton mPhotoButton;
    private EditText mWeightDisplay;
    private TextView mDateDisplay;
    public UUID clientId;
    private Button mSendPictureButton;
    private Client mClient;

    public static final String EXTRA_WEIGHIN_DATE = "weigh in date";
    public static final String EXTRA_CLIENT_ID = "weigh in fragment extra client fragment";
    private static final String DIALOG_IMAGE = "image for picture click";
    private static final int REQUEST_PHOTO = 0;
    private static final String TAG = "weigh in fragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Date weighInDate = (Date)getArguments().getSerializable(EXTRA_WEIGHIN_DATE);
        clientId = UUID.fromString(getArguments().getSerializable(EXTRA_CLIENT_ID).toString());
        mClient = ClientManager.get(getActivity()).getClient(clientId);
        mWeighIn = WeighInManager.get(getActivity(), clientId).getWeighIn(weighInDate);
    }

    @Override
    public void onStart(){
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop(){
        super.onStop();
        PictureUtils.cleanImageView(mPhotoDisplay);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_weighin, parent, false);

        mPhotoDisplay = (ImageView)v.findViewById(R.id.weighin_picture);
        mPhotoDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo p = mWeighIn.getPhoto();
                if (p == null)
                    return;

                FragmentManager fm = getActivity().getFragmentManager();
                String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path)
                        .show(fm, DIALOG_IMAGE);
            }
        });

        mWeightDisplay = (EditText)v.findViewById(R.id.weighin_weight);
        mWeightDisplay.setText(String.valueOf(mWeighIn.getWeight()));
        mWeightDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    mWeighIn.setWeight(Integer.parseInt(s.toString()));
                } catch (NumberFormatException e){

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateDisplay = (TextView)v.findViewById(R.id.weighin_date_display);
        mDateDisplay.setText(mWeighIn.getDate().toString());

        mPhotoButton = (ImageButton)v.findViewById(R.id.take_picture_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        PackageManager pm = getActivity().getPackageManager();
        boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasACamera){
            mPhotoButton.setEnabled(false);
        }

        mSendPictureButton = (Button)v.findViewById(R.id.send_email_button);
        mSendPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{mClient.getEmail()});
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(getActivity().getFileStreamPath(mWeighIn.getPhoto().getFilename()).getPath())));
                i.setType("image/jpg");
                startActivity(i);
            }
        });

        //return view
        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        WeighInManager.get(getActivity(), clientId).saveWeighIns();
    }

    private void showPhoto(){
        Photo p = mWeighIn.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoDisplay.setImageDrawable(b);
    }
    public static WeighInFragment newInstance(Date weighInDate, UUID clientId){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_WEIGHIN_DATE, weighInDate);
        args.putSerializable(EXTRA_CLIENT_ID, clientId.toString());

        WeighInFragment fragment = new WeighInFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_PHOTO){
            String filename = data.getStringExtra(CameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null){
                Photo p = new Photo(filename);
                mWeighIn.setPhoto(p);
                showPhoto();
            }
        }
    }

}
