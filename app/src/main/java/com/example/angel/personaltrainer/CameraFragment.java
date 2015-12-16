package com.example.angel.personaltrainer;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class CameraFragment extends Fragment
//creates a live preview of a picture and takes it + saves it to apps data file. taken from BNR photo chapters.
{
    private static final String TAG = "personal trainer camera";
    public static final String EXTRA_PHOTO_FILENAME = "weigh in photo filename";

    private Camera mCamera;
    private SurfaceView mSurfaceView;

    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_camera, parent, false);

        Button takePictureButton = (Button)v.findViewById(R.id.take_picture_button);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null){
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }

            }
        });
        mSurfaceView = (SurfaceView)v.findViewById(R.id.camera_preview);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if (mCamera != null){
                        mCamera.setPreviewDisplay(holder);
                    }
                } catch (IOException ioe){
                    Log.e(TAG, "Error setting up preview display", ioe);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
                if (mCamera == null) return;

                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
                parameters.setPreviewSize(s.width, s.height);
                s = getBestSupportedSize(parameters.getSupportedPictureSizes(), w, h);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try{
                    mCamera.startPreview();
                } catch (Exception e){
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null){
                    mCamera.stopPreview();
                }

            }
        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        mCamera = Camera.open(0);
    }

    @Override
    public void onPause(){
        super.onPause();
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes){
            int area = s.width * s.height;
            if (area > largestArea){
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

  private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
      @Override
      public void onPictureTaken(byte[] data, Camera camera) {
          String filename = UUID.randomUUID().toString() + ".jpg";
          FileOutputStream os = null;
          boolean success = true;

          try{
              os = getActivity().openFileOutput(filename, Context.MODE_WORLD_READABLE);
              os.write(data);
          } catch (Exception e){
              Log.e(TAG, "Error writing to file " + filename, e);
              success = false;
          } finally {
              try {
                  if (os != null)
                      os.close();
              } catch (Exception e) {
                  Log.e(TAG, "Error closing file " + filename, e);
                  success = false;
              }
          }
          if (success){
              Intent i = new Intent();
              i.putExtra(EXTRA_PHOTO_FILENAME, filename);
              getActivity().setResult(Activity.RESULT_OK, i);
          } else {
              getActivity().setResult(Activity.RESULT_CANCELED);
          }
          getActivity().finish();
      }
  };

}
