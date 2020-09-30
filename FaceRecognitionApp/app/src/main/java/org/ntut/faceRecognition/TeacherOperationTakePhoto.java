package org.ntut.faceRecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TeacherOperationTakePhoto extends AppCompatActivity {

    ArrayList<String> student_fake_name = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_operation_take_photo);

        student_fake_name.add("陳小一");
        student_fake_name.add("王小二");
        student_fake_name.add("李小三");
        student_fake_name.add("許小四");
        
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

            case R.id.button_return:
                startActivity(intent);

                break;

            case R.id.gray_return_button:
                intent.setClass(this , TeacherClass.class);
                break;
        }
    }
}