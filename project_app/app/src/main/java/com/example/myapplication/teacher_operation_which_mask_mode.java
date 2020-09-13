package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
public class teacher_operation_which_mask_mode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_which_mask_mode);
    }

    public void onclick(View v) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        switch(v.getId()){

            case R.id.button_mask_mode:
                startActivity(intent);

                break;

            case R.id.gray_return_button:
                intent.setClass(this , teacher_operation.class);
                break;
        }
    }
}