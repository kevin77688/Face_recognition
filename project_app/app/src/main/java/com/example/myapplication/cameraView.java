package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class cameraView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        // Initial python env
        initPython();
        Toast toast = Toast.makeText(getApplicationContext(), pyFile(), Toast.LENGTH_SHORT);
        toast.show();
    }

    private void initPython() {
        if (!Python.isStarted())
            Python.start(new AndroidPlatform(this));
    }

    private String pyFile() {
        Python python = Python.getInstance();
        PyObject pythonFile = python.getModule("testFile");
        return pythonFile.callAttr("helloWorld").toString();
    }
}