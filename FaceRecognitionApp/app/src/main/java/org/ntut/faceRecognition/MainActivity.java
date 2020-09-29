package org.ntut.faceRecognition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginView(View v) {
        Intent intent = new Intent();
        intent.setClass(this, Login.class);
        startActivity(intent);
    }

    public void captureView(View v){
        Intent intent = new Intent();
        intent.setClass(this, CameraCapture.class);
        startActivity(intent);
    }
}