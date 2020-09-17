package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeacherOperationWithMask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_with_mask);

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
                intent.setClass(this , TeacherOperation.class);
                break;
        }
    }
}