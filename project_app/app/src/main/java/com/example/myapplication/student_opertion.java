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
        TextView wel_text = (TextView) findViewById(R.id.wel_text);
        Button btn_upload = (Button)findViewById((R.id.upload_button));
        email = userdata.getEmail();
        wel_text.setText("歡迎" + userdata.getName() + "學生");
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickUpload();
            }
        });
    }

    protected  void ClickUpload(){
        Intent intent = new Intent();
        intent.setClass(this, student_upload.class);
        startActivity(intent);
    }

}