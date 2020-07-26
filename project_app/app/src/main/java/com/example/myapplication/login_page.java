package com.example.myapplication;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;
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

