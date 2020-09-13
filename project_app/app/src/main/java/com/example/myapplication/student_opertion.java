package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class student_opertion extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_opertion);
        GlobalVariable userdata = (GlobalVariable) getApplicationContext();
        TextView upview_text = (TextView) findViewById(R.id.upview_text);
        email = userdata.getEmail();
        upview_text.setText("\n歡迎" + userdata.getName() + "學生");
    }
    public void onclick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.dcchect_photo_button:
                intent.setClass(this , loginTest.class);
                break;
            case R.id.upload_button:
                intent.setClass(this, student_upload.class);
                break;
            case R.id.check_presentation_record_button:
//                intent.setClass(this , teacher_login_new.class);
                break;
            case R.id.return_button:
                intent.setClass(this , loginTest.class);
                break;
        }
        startActivity(intent);
    }

}