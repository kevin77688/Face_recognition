package org.ntut.faceRecognition.Camera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.ntut.faceRecognition.R;
import org.ntut.faceRecognition.Utility.ImageSaver;
import org.ntut.faceRecognition.Utility.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CameraPhotoSelection extends AppCompatActivity {

    private static final int GET_PHOTO_FORM_CAMERA = 1;
    private Button cameraButton, confirmButton;
    private ImageView captureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo_selection);

        findView();
        setCameraButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.e("Request code", Integer.toString(requestCode));
        Log.e("GET_PHOTO_FORM_CAMERA", Integer.toString(requestCode));
        if (requestCode == GET_PHOTO_FORM_CAMERA) {
            if(resultCode == Activity.RESULT_OK){
                try{
                    Bitmap bitmap = new ImageSaver(this).
                            setFileName("captureImage.png").
                            setDirectoryName("images").
                            load();
                    bitmap = Bitmap.createScaledBitmap(bitmap,  600 ,600, true);
                    captureView.setImageBitmap(bitmap);
                    captureView.invalidate();
                    captureView.postInvalidate();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                Utils.showToast("Canceled", CameraPhotoSelection.this);
            }
        }
    }

    private void findView(){
        cameraButton = findViewById(R.id.take_photo_button);
        confirmButton = findViewById(R.id.confirm_button);
        captureView = findViewById(R.id.capture_view);
    }

    private void setCameraButton(){
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage(CameraCapture.class);
            }
        });
    }

    private void gotoPage(Class c) {
        Intent intent = new Intent();
        intent.setClass(CameraPhotoSelection.this, c);
        startActivityForResult(intent, GET_PHOTO_FORM_CAMERA);
    }
}