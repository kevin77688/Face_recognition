package org.ntut.faceRecognition;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.JavaCameraView;

public class CameraCapture extends AppCompatActivity {

    private JavaCameraView mJavaCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_capture);

        mJavaCameraView = findViewById(R.id.javaCamView);
        mJavaCameraView.setCameraIndex(0);
        mJavaCameraView.setCvCameraViewListener(new FaceDetector(this));
        mJavaCameraView.enableView();
    }
}