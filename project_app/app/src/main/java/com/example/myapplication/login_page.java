package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
public class login_page extends AppCompatActivity {

    private TextView TextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void clickToReturn(View v) {
        Intent intent = new Intent();
        intent.setClass(this , MainActivity.class);

        startActivity(intent);
    }

    public void clickToTeacherLogin(View v) {
        Intent intent = new Intent();
        intent.setClass(this , teacher_login_new.class);

        startActivity(intent);
    }
}
