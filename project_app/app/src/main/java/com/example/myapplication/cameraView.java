package com.example.myapplication;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.opencv.android.JavaCameraView;


public class cameraView extends AppCompatActivity {

    private JavaCameraView mJavaCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        mJavaCameraView = findViewById(R.id.javaCamView);
        mJavaCameraView.setCameraIndex(1);
        mJavaCameraView.setCvCameraViewListener(new faceDetector(this));
        mJavaCameraView.enableView();
    }


    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(cameraView.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


}