package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
public class teacher_login_new extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login_new);
        EditText edt_teacher =(EditText)findViewById(R.id.editText_teacher_name);
        EditText edt_passward =(EditText)findViewById(R.id.editText_teacher_passward);
    }

    public void clickToReturn(View v) {
        Intent intent = new Intent();
        intent.setClass(this , login_page.class);
        startActivity(intent);
    }

    public void clickClean(View v) {
        EditText edt =(EditText)findViewById(v.getId());
        edt.setHint(null);
    }

    public void clickToTeacherOperation(View v) {
        Intent intent = new Intent();
        intent.setClass(this , teacher_operation.class);
        EditText editText_teacher_name = (EditText)findViewById(R.id.editText_teacher_name);
        intent.putExtra("teacher_name", editText_teacher_name.getText().toString().trim());
        startActivity(intent);
    }
}