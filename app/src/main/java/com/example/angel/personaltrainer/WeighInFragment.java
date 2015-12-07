package com.example.angel.personaltrainer;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class WeighInFragment extends Fragment {
    private WeighIn mWeighIn;
    private Photo mPhoto;
    private ImageView mPhotoDisplay;
    private ImageButton mPhotoButton;
    private EditText mWeightDisplay;
    private TextView mDateDisplay;

    public static final String EXTRA_WEIGHIN_DATE = "weigh in date";
    private static final int REQUEST_PHOTO = 0;
    private static final String TAG = "weigh in fragment";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Date weighInDate = (Date)getArguments().getSerializable(EXTRA_WEIGHIN_DATE);
        mWeighIn = WeighInManager.get(getActivity()).getWeighIn(weighInDate);
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

        mWeightDisplay = (EditText)v.findViewById(R.id.weighin_weight);
        mWeightDisplay.setText(String.valueOf(mWeighIn.getWeight()));
        mWeightDisplay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mWeighIn.setWeight(Integer.parseInt(s.toString()));
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

        //return view
        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        WeighInManager.get(getActivity()).saveWeighIns();
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
    public static WeighInFragment newInstance(Date weighInDate){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_WEIGHIN_DATE, weighInDate);

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
