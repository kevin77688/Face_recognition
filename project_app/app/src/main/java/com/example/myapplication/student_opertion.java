package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class student_opertion extends AppCompatActivity {
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_opertion);
        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
        TextView wel_text = (TextView)findViewById(R.id.wel_text);
        email = userdata.getEmail();
        wel_text.setText("歡迎"+userdata.getName()+"學生");
    }
}