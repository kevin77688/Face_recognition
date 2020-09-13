package com.example.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class teacher_operation_which_mask_mode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_which_mask_mode);

        LinearLayout mainLinerLayout = (LinearLayout) this.findViewById(R.id.roll_call_layout);
        for (int i = 0;i<50;i++){
            TextView textview=new TextView(this);
            textview.setTextSize(30);
            textview.setWidth(250);   //設定寬度
            textview.setHeight(70);
            textview.setGravity(Gravity.CENTER);
            textview.setText("你好！"+i);
            mainLinerLayout.addView(textview);
        }
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