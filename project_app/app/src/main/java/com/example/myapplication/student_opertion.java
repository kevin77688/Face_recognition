package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class student_opertion extends AppCompatActivity {
    private String email;
//    email = loginTest.user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_opertion);
        GlobalVariable userdata = (GlobalVariable)getApplicationContext();
        email = userdata.getEmail();
        Log.d("email", email);
    }
}